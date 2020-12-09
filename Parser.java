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
                for (Symbol symbol : activeString) {
                    for (Rule rule : rules) {
                        String ruleString = rule.getVariable().toString();
                        if (symbol.toString().equals(ruleString)) {
                            newActiveStrings.add(rule.getExpansion());
                            System.out.println(newActiveStrings);

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

      /*if rule applies to string
      newActiveStrings.add(all strings with the rule applied)
      activeStrings = newActiveStrings;
    }
    if (targetString is in activeStrings){
    if (boolean contains = Arrays.stream(activeStrings).anyMatch(targetString::equals)) {
      return contains;
  }*/

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