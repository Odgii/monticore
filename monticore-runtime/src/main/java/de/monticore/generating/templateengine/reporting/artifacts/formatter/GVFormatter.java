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

package de.monticore.generating.templateengine.reporting.artifacts.formatter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import de.monticore.generating.templateengine.reporting.artifacts.model.APkg;
import de.monticore.generating.templateengine.reporting.artifacts.model.Element;
import de.monticore.generating.templateengine.reporting.artifacts.model.ElementType;
import de.monticore.generating.templateengine.reporting.artifacts.model.Pkg;
import de.monticore.generating.templateengine.reporting.artifacts.model.RootPkg;



/**
 * TODO: Write me!
 * 
 * @author (last commit) $Author$
 * @version $Revision$, $Date$
 */
public class GVFormatter extends AFormatter {
  
  private Map<ElementType, String> shapes = new HashMap<ElementType, String>();
  
  public GVFormatter() {
    this.shapes.put(ElementType.HELPER, "cds");
    this.shapes.put(ElementType.MODEL, "box3d");
    this.shapes.put(ElementType.TEMPLATE, "ellipse");
    this.shapes.put(ElementType.FILE, "note");
  }
  
  private List<String> getAllLinkContent(APkg pkg) {
    List<String> lines = Lists.newArrayList();
    
    for (Element e : pkg.getElements()) {
      lines.addAll(getLinkContent(e));
    }
    
    for (Pkg p : pkg.getSubPkgs()) {
      lines.addAll(getAllLinkContent(p));
    }
    
    return lines;
  }
  
  private List<String> getContent(Element element) {
    List<String> lines = Lists.newArrayList();
    addLine(lines, "node[shape=" + getShape(element.getType()) + "];");
    addLine(lines, getUniqueName(element) + " [label=\"" + element.getFullQualifiedName() + " ("
        + element.getNumberOfCalls() + ")\"];");
    return lines;
  }
  
  private List<String> getLinkContent(Element element) {
    List<String> lines = Lists.newArrayList();
    
    for (Element link : element.getLinks()) {
      addLine(lines, getUniqueName(element) + " -> " + getUniqueName(link) + ";");
    }
    return lines;
  }
  
  private String getUniqueName(Element e) {
    return e.getType().getName() + "_"
        + e.getQualifiedName().replaceAll("[.]", "_").replaceAll("-", "_");
  }
  
  /**
   * Print package, subpackages and elements to open output file
   */
  public List<String> getElementContent(APkg pkg) {
    List<String> lines = Lists.newArrayList();
    
    if (pkg.hasElements()) {
      addLine(lines, "subgraph cluster_" + pkg.getQualifiedName().replace(".", "_") + " {");
      indent();
      addLine(lines, "label = \"" + pkg.getQualifiedName() + "\";");
      addLine(lines, "labeljust = l;");
    }
    
    // Recurse
    for (Pkg subPkg : pkg.getSubPkgs()) {
      lines.addAll(getElementContent(subPkg));
    }
    
    for (Element e : pkg.getElements()) {
      lines.addAll(getContent(e));
    }
    
    if (pkg.hasElements()) {
      unindent();
      addLine(lines, "}");
    }
    return lines;
  }
  
  
  /**
   * Get the dotgraph shape of a type
   */
  public String getShape(ElementType type) {
    if (this.shapes.containsKey(type)) {
      return this.shapes.get(type);
    }
    else {
      return "hexagon";
    }
  }

  /**
   * @see mc.codegen.reporting.visualization.printer.AFormatter#getContent(mc.codegen.reporting.visualization.model.RootPkg)
   */
  @Override
  public List<String> getContent(RootPkg rootPkg) {
    List<String> lines = Lists.newArrayList();
    lines.add("digraph {");
    indent();
    lines.addAll(getElementContent(rootPkg));
    lines.addAll(getAllLinkContent(rootPkg));
    unindent();
    lines.add("}");
    return lines;
  }

}
