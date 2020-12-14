import computation.contextfreegrammar.*;
import computation.parser.*;
import computation.parsetree.*;
import computation.derivation.*;

import java.util.*;

public class Parser implements IParser {

    public boolean isInLanguage(ContextFreeGrammar cfg, Word w) {
        ParseTreeNode isInLanguage = generateParseTree(cfg, w);
        return (isInLanguage != null);
    }

    public ParseTreeNode generateParseTree(ContextFreeGrammar cfg, Word w) {
        List<Derivation> activeDerivations = new ArrayList<>();
        List<Rule> rules;
        rules = cfg.getRules();

        ParseTreeNode emptyTree = null;
        Variable startVariable = cfg.getStartVariable();
        Word startWord = new Word(startVariable);
        Derivation startDerivation = new Derivation(startWord);
        activeDerivations.add(startDerivation);

        Derivation acceptedDerivation = null;
        boolean inLanguage = false;
        boolean emptyStringRule = false;

        int steps = 0;
        int n = w.length();

        // special case for empty string
        if (n == 0) {
            String empty = w.toString();
            for (Rule rule : rules) {
                if (empty.equals(rule.getExpansion().toString())) {
                    Variable v = rule.getVariable();
                    inLanguage = true;
                    emptyTree = ParseTreeNode.emptyParseTree(v);
                    emptyStringRule = true;
                }
            }
        }

        while (steps < (2 * n - 1)) {
            List<Derivation> newActiveDerivations = new ArrayList<>();
            for (Derivation activeDerivation : activeDerivations) {
                Word latest = activeDerivation.getLatestWord();
                for (int i = 0; i < latest.length(); i++) {
                    Symbol symbol = latest.get(i);
                    for (Rule rule : rules) {
                        Variable ruleVariable = rule.getVariable();
                        if (symbol.equals(ruleVariable)) {
                            Word expansion = rule.getExpansion();
                            Word replaceWord = latest.replace(i, expansion);
                            Derivation replaceDerivation = new Derivation(activeDerivation);
                            replaceDerivation.addStep(replaceWord, rule, i);
                            newActiveDerivations.add(replaceDerivation);
                        }
                    }
                }
            }
            activeDerivations = newActiveDerivations;
            steps++;
        }

        for (Derivation d : activeDerivations) {
            if (d.getLatestWord().equals(w)) {
                inLanguage = true;
                acceptedDerivation = d;
                break;
            }
        }

        if (inLanguage && !emptyStringRule) {
            return buildParseTree(acceptedDerivation);
        } else if (emptyStringRule) {
            return emptyTree;
        }
        return null;
    }

    private ParseTreeNode buildParseTree(Derivation derivation) {
        Word latest = derivation.getLatestWord();

        // build a list of ParseTreeNodes from each of the terminals in the word
        List<ParseTreeNode> treeNodes = new LinkedList<>();
        for (int i = 0; i < latest.length(); i++) {
            Symbol s = latest.get(i);
            if (s.isTerminal()) {
                ParseTreeNode terminal = new ParseTreeNode(s);
                treeNodes.add(i, terminal);
            }
        }

        // loop through the steps in reverse order to build tree from bottom up
        for (Step step : derivation) {
            // use index of the step to merge two of the ParseTreeNodes into a single node (so the two nodes become the children)
            int i = step.getIndex();
            if (!step.isStartSymbol()) {
                Symbol w = step.getRule().getVariable();
                if (step.getRule().getExpansion().isTerminal()) {
                    ParseTreeNode updatedNode = new ParseTreeNode(w, treeNodes.get(i));
                    treeNodes.remove(i);
                    treeNodes.add(i, updatedNode);
                } else {
                    ParseTreeNode updatedNodes = new ParseTreeNode(w, treeNodes.get(i), treeNodes.get(i + 1));
                    treeNodes.remove(i);
                    treeNodes.add(i, updatedNodes);
                }
            }
        }
        return treeNodes.get(0);
    }
}



