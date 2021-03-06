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

package de.monticore.grammar.cocos;

import de.monticore.grammar.grammar._ast.ASTProd;
import de.monticore.grammar.grammar._cocos.GrammarASTProdCoCo;
import de.se_rwth.commons.logging.Log;

/**
 * Created by
 *
 * @author KH
 */
public class ProdStartsWithCapital implements GrammarASTProdCoCo {

  public static final String ERROR_CODE = "0xA4031";

  public static final String ERROR_MSG_FORMAT = " The nonterminal %s should not start with a lower-case letter.";

  @Override
  public void check(ASTProd node) {
    if(Character.isLowerCase(node.getName().charAt(0))){
      Log.error(String.format(ERROR_CODE + ERROR_MSG_FORMAT, node.getName()), node.get_SourcePositionStart());
    }
  }
}
