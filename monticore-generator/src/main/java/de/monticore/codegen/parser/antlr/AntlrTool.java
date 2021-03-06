/*
 * ******************************************************************************
 * MontiCore Language Workbench
 * Copyright (c) 2015, MontiCore, All rights reserved.
 *
 * This project is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this project. If not, see <http://www.gnu.org/licenses/>.
 * ******************************************************************************
 */

package de.monticore.codegen.parser.antlr;

import java.nio.file.Path;

import org.antlr.v4.Tool;
import org.antlr.v4.tool.ANTLRMessage;
import org.antlr.v4.tool.Grammar;
import org.antlr.v4.tool.ast.GrammarRootAST;
import org.stringtemplate.v4.ST;

import com.google.common.base.Preconditions;

import de.monticore.languages.grammar.MCGrammarSymbol;
import de.monticore.languages.grammar.MCRuleSymbol;
import de.se_rwth.commons.SourcePosition;
import de.se_rwth.commons.StringTransformations;
import de.se_rwth.commons.logging.Log;

/**
 * ANTLR parser generator
 *
 * @author (last commit) $Author$
 * @version $Revision$, $Date$
 */
public class AntlrTool extends Tool {
  
  private MCGrammarSymbol grammarSymbol;
  
  public AntlrTool(String[] args, MCGrammarSymbol grammarSymbol, Path outputDir) {
    super(args);
    this.grammarSymbol = grammarSymbol;
    Preconditions.checkArgument(outputDir.toFile().exists(),
        "The potput directory for AntlrTool " +
            outputDir + " doesn't exist.");
    this.outputDirectory = outputDir.toString();
    this.haveOutputDir = true;
    handleArgs();
  }
  
  @Override
  public void error(ANTLRMessage message) {
    createMessage(message, true);
  }
  
  @Override
  public void warning(ANTLRMessage message) {
    createMessage(message, false);
  }
  
  /**
   * Parses the given ANTLR grammar and generates parser
   * 
   * @param inputFile - ANTLR grammar
   */
  public void createParser(String inputFile) {
    Grammar grammar = parseAntlrFile(inputFile);
    generateParser(grammar);
  }
  
  /**
   * Creates a grammar object associated with the ANTLR grammar AST.
   * 
   * @param inputFile - ANTLR grammar
   * @return a grammar object associated with the ANTLR grammar AST
   */
  public Grammar parseAntlrFile(String inputFile) {
    GrammarRootAST ast = parseGrammar(inputFile);
    Grammar grammar = createGrammar(ast);
    grammar.fileName = inputFile;
    return grammar;
  }
  
  /**
   * Generates ANTLR Parser
   * 
   * @param antlrGrammar
   */
  public void generateParser(Grammar antlrGrammar) {
    process(antlrGrammar, true);
  }
  
  /**
   * Prints a message in MC style
   * 
   * @param message
   * @param isError
   */
  private void createMessage(ANTLRMessage message, boolean isError) {
    // Set default position
    SourcePosition position = SourcePosition.getDefaultSourcePosition();
    
    ST msgST = errMgr.getMessageTemplate(message);
    String origMessage = msgST.render();
    Log.debug(origMessage, "AnltrTool");
    
    // Change arguments corresponding to names in MC grammar
    Object[] args = message.getArgs();
    for (int i = 0; i < args.length; i++) {
      if (args[i] instanceof String) {
        String name = StringTransformations.capitalize((String) args[i]);
        MCRuleSymbol rule = grammarSymbol.getRule(name);
        if (rule != null) {
          args[i] = name;
          if (i == 0) {
            position = rule.getSourcePosition();
          }
        }
      }
    }
    
    // Create message
    ST messageST = message.getMessageTemplate(false);
    String output = "0xA0129 " + messageST.render();
    
    // Print message
    if (isError) {
      if (position.equals(SourcePosition.getDefaultSourcePosition())) {
        Log.error(output);
      }
      else {
        Log.error(output, position);
      }
    }
    else {
      if (position.equals(SourcePosition.getDefaultSourcePosition())) {
        Log.warn(output);
      }
      else {
        Log.warn(output, position);
      }
    }
  }
  
}
