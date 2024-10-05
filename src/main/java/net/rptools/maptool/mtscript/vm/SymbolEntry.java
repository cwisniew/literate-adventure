/*
 * This software Copyright by the RPTools.net development team, and
 * licensed under the Affero GPL Version 3 or, at your option, any later
 * version.
 *
 * MapTool Source Code is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * You should have received a copy of the GNU Affero General Public
 * License * along with this source Code.  If not, please visit
 * <http://www.gnu.org/licenses/> and specifically the Affero license
 * text at <http://www.gnu.org/licenses/agpl.html>.
 */
package net.rptools.maptool.mtscript.vm;

import net.rptools.maptool.mtscript.vm.values.Symbol;

/// A symbol table entry.
/// This class is used to store information about a symbol in the symbol table.
/// This includes the symbol itself, whether it is a constant, and the scope level at which it was
// defined.
/// @param symbol The symbol.
/// @param constant Whether the symbol is a constant.
/// @param scopeLevel The scope level at which the symbol was defined.
/// @param index The index of the symbol in the symbol table.
/// @param compileTimeConstant Whether the symbol is a compile-time constant.
public record SymbolEntry(Symbol symbol, boolean constant, int scopeLevel, int index,
                          boolean compileTimeConstant) {}
