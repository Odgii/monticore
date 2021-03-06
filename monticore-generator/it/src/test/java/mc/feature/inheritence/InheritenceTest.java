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

package mc.feature.inheritence;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;

import org.junit.Test;

import de.monticore.antlr4.MCConcreteParser.ParserExecution;
import mc.GeneratorIntegrationsTest;
import mc.feature.inheritence.inheritence._ast.ASTA;
import mc.feature.inheritence.inheritence._ast.ASTB;
import mc.feature.inheritence.inheritence._ast.ASTC;
import mc.feature.inheritence.inheritence._ast.ASTD;
import mc.feature.inheritence.inheritence._ast.ASTIG;
import mc.feature.inheritence.inheritence._ast.ASTIH;
import mc.feature.inheritence.inheritence._ast.ASTIM;
import mc.feature.inheritence.inheritence._ast.ASTK;
import mc.feature.inheritence.inheritence._ast.ASTL;
import mc.feature.inheritence.inheritence._ast.ASTXAE;
import mc.feature.inheritence.inheritence._ast.ASTXAO;
import mc.feature.inheritence.inheritence._ast.ASTXF;
import mc.feature.inheritence.inheritence._ast.ASTXP;
import mc.feature.inheritence.inheritence._parser.InheritenceParser;

public class InheritenceTest extends GeneratorIntegrationsTest {
  
  // // Test1 : IG should parse all "a", "b", and "c"
  // A(IF) = "a" ;
  //
  // B(IF) = "b" ;
  //
  // C(IG) = "c";
  // interface IF (IG);
  
  @Test
  public void test1a() throws IOException {
    
    InheritenceParser parser = new InheritenceParser();
    parser.setParserTarget(ParserExecution.EOF);
    
    Optional<ASTIG> ast = parser.parseIG(new StringReader("a"));
    
    assertTrue(ast.get() instanceof ASTA);
    
  }
  
  @Test
  public void test1b() throws IOException {
    
    InheritenceParser parser = new InheritenceParser();
    parser.setParserTarget(ParserExecution.EOF);
    
    Optional<ASTIG> ast = parser.parseIG(new StringReader("b"));
    
    assertTrue(ast.get() instanceof ASTB);
    
  }
  
  @Test
  public void test1c() throws IOException {
    
    InheritenceParser parser = new InheritenceParser();
    parser.setParserTarget(ParserExecution.EOF);
    
    Optional<ASTIG> ast = parser.parseIG(new StringReader("c"));
    
    assertTrue(ast.get() instanceof ASTC);
    
  }
  
  // // Test 2 : IH should parse "d" (calls D with parameters null)
  // interface IH = A ;
  //
  // D [B:B] =
  // "d";
  @Test
  public void test2() throws IOException {
    
    InheritenceParser parser = new InheritenceParser();
    parser.setParserTarget(ParserExecution.EOF);
    
    Optional<ASTIH> ast = parser.parseIH(new StringReader("d"));
    assertTrue(ast.get() instanceof ASTD);
    
  }
  
  // Test 3 : IM should parse "aa", "bb" and "ab" (predicate is necessary
  // for k=1)
  //
  // K(("a" "a" | "b" "b")=> IM) = "a" "a" | "b" "b";
  // L(IM) = "a" "b";
  // }
  @Test
  public void test3a() throws IOException {
    
    InheritenceParser parser = new InheritenceParser();
    parser.setParserTarget(ParserExecution.EOF);
    
    Optional<ASTIM> ast = parser.parseIM(new StringReader("aa"));
    assertTrue(ast.get() instanceof ASTK);
  }
  
  @Test
  public void test3b() throws IOException {
    
    InheritenceParser parser = new InheritenceParser();
    parser.setParserTarget(ParserExecution.EOF);
    
    Optional<ASTIM> ast = parser.parseIM(new StringReader("bb"));
    assertTrue(ast.get() instanceof ASTK);
  }
  
  @Test
  public void test3c() throws IOException {
    
    InheritenceParser parser = new InheritenceParser();
    parser.setParserTarget(ParserExecution.EOF);
    
    Optional<ASTIM> ast = parser.parseIM(new StringReader("ab"));
    assertTrue(ast.get() instanceof ASTL);
    
  }
  
  // Test 4 : XAE should parse "f" and return an XF
  //
  @Test
  public void test4a() throws IOException {
    
    InheritenceParser parser = new InheritenceParser();
    parser.setParserTarget(ParserExecution.EOF);
    
    Optional<ASTXAE> ast = parser.parseXAE(new StringReader("f"));
    assertTrue(ast.get() instanceof ASTXF);
  }
  
  // Test 5 : XAO should parse "p" but not "q" and return an XP
  //
  @Test
  public void test5a() throws IOException {
    
    InheritenceParser parser = new InheritenceParser();
    parser.setParserTarget(ParserExecution.EOF);
    
    Optional<ASTXAO> ast = parser.parseXAO(new StringReader("p"));
    assertTrue(ast.get() instanceof ASTXP);
    assertFalse(parser.hasErrors());
    
  }
  
  @Test
  public void test5b() throws IOException {
    
    InheritenceParser parser = new InheritenceParser();
    parser.setParserTarget(ParserExecution.EOF);
    
    parser.parseXAO(new StringReader("q"));
    assertTrue(parser.hasErrors());
  }
 
}
