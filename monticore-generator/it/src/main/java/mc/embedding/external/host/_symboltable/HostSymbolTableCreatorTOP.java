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

/* generated from model null*/
/* generated by template symboltable.SymbolTableCreator*/




package mc.embedding.external.host._symboltable;

import de.se_rwth.commons.logging.Log;

import mc.embedding.external.host._ast.ASTHost;
import mc.embedding.external.host._visitor.HostVisitor;
import de.monticore.symboltable.MutableScope;
import de.monticore.symboltable.ResolverConfiguration;
import de.monticore.symboltable.Scope;
import java.util.Deque;

public class HostSymbolTableCreatorTOP extends de.monticore.symboltable.CommonSymbolTableCreator
         implements HostVisitor {

  public HostSymbolTableCreatorTOP(
    final ResolverConfiguration resolverConfig, final MutableScope enclosingScope) {
    super(resolverConfig, enclosingScope);
  }

  public HostSymbolTableCreatorTOP(final ResolverConfiguration resolverConfig, final Deque<MutableScope> scopeStack) {
    super(resolverConfig, scopeStack);
  }

  /**
  * Creates the symbol table starting from the <code>rootNode</code> and
  * returns the first scope that was created.
  *
  * @param rootNode the root node
  * @return the first scope that was created
  */
  public Scope createFromAST(ASTHost rootNode) {
    Log.errorIfNull(rootNode, "0xA7004_587 Error by creating of the HostSymbolTableCreatorTOP symbol table: top ast node is null");
    rootNode.accept(this);
    return getFirstCreatedScope();
  }

}
