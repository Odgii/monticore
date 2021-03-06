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

package de.monticore.languages.grammar;

import java.util.Optional;

import de.monticore.grammar.grammar._ast.ASTRuleComponent;

public class PredicatePair {
  private String classname;
  
  private Optional<ASTRuleComponent> component;
  
  public Optional<ASTRuleComponent> getComponent() {
    return component;
  }
  
  public void setBlock(ASTRuleComponent block) {
    this.component = Optional.ofNullable(block);
  }
  
  public String getClassname() {
    return classname;
  }
  
  public PredicatePair(String classname, ASTRuleComponent block) {
    this.classname = classname;
    this.component = Optional.ofNullable(block);
  }
  
  @Override
  public boolean equals(Object o) {
    return (o instanceof PredicatePair) && classname.equals(((PredicatePair) o).classname);
  }
  
  @Override
  public int hashCode() {
    return classname.hashCode();
  }
}
