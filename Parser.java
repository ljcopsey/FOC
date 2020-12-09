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
        List<Word> activeStrings = new ArrayList<>();
        List<Rule> rules;
        rules = cfg.getRules();

        Variable startVariable = cfg.getStartVariable();
        Word startWord = new Word(startVariable);
        activeStrings.add(startWord);

//    Variable S = MyGrammar.courseworkCNF().getStartVariable();
//    Word start = new Word(S);
//    activeStrings.add(start);

        int steps = 0;
        int n = w.length();
        while (steps < (2 * n - 1)) {
            List<Word> newActiveStrings = new ArrayList<>();
            for (Word activeString : activeStrings) {
                for (int i = 0; i < activeString.length(); i++) {
                    Symbol symbol = activeString.get(i);
                    for (Rule rule : rules) {
                        Variable ruleVariable = rule.getVariable();
                        if (symbol.equals(ruleVariable)) {
                            Word expansion = rule.getExpansion();
                            Word replace = activeString.replace(i, expansion);
                            newActiveStrings.add(replace);
                        }
                    }
                }
            }
            activeStrings = newActiveStrings;
            steps++;
        }
        System.out.println(activeStrings);
        return activeStrings.contains(w);
    }

    public ParseTreeNode generateParseTree(ContextFreeGrammar cfg, Word w) {
        if (this.isInLanguage(cfg, w)) {
            //generate parse tree
            generateParseTree(cfg, w);
        } else {
            System.out.println("Word is not in grammar");
        }
        return null;
    }
}
