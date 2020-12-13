import computation.contextfreegrammar.*;
import computation.parser.*;
import computation.parsetree.*;
import computation.derivation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Parser implements IParser {
    public boolean isInLanguage(ContextFreeGrammar cfg, Word w) {
        return false;
    }

    public ParseTreeNode generateParseTree(ContextFreeGrammar cfg, Word w) {
        List<Derivation> activeDerivations = new ArrayList<>();
        List<Rule> rules;
        rules = cfg.getRules();

        Variable startVariable = cfg.getStartVariable();
        Word startWord = new Word(startVariable);
        Derivation derivations = new Derivation(startWord);
        activeDerivations.add(derivations);

        List<ParseTreeNode> children = new ArrayList<>();
        ParseTreeNode treeNode;

        int steps = 0;
        int n = w.length();
        while (steps < (2 * n - 1)) {
            List<Derivation> newActiveDerivations = new ArrayList<>();
            for (Derivation activeDerivation : activeDerivations) {
                for (int i = 0; i < activeDerivation.getLatestWord().length(); i++) {
                    Symbol symbol = activeDerivation.getLatestWord().get(i);
                    for (Rule rule : rules) {
                        Variable ruleVariable = rule.getVariable();
                        if (symbol.equals(ruleVariable)) {
                            Word expansion = rule.getExpansion();
                            Word replaceWord = activeDerivation.getLatestWord().replace(i, expansion);
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
        if (activeDerivations.contains(w)) {
            for (Step derivation : derivations) {
                if (derivation.getRule().getExpansion().isTerminal()) {
                    do {
                        int i = derivation.getIndex();
                        Rule rule = derivation.getRule();
                        Symbol symbol = rule.getVariable();
                        ParseTreeNode parseTreeNode = new ParseTreeNode(symbol);
                        children.add(parseTreeNode);
                    }
                    while (!derivation.isStartSymbol());
                }
            }
        }
        return ParseTreeNode.emptyParseTree(startVariable);
    }
}
