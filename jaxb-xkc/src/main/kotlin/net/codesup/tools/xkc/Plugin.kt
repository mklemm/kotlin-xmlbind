package net.codesup.tools.xkc

import net.codesup.tools.xkc.model.code.CodeModel
import net.codesup.tools.xkc.model.grammar.GrammarModel
import net.codesup.tools.xkc.model.logical.LogicalModel
import net.codesup.tools.xkc.model.schema.SchemaModel

/**
 * @author Mirko Klemm 2024-05-03
 *
 */
interface Plugin {
    fun activate(options: Options)
    fun grammarsLoaded(options: Options, grammarModel: GrammarModel):GrammarModel
    fun schemaModelBuilt(options: Options, schemaModel: SchemaModel):SchemaModel
    fun logicalModelBuilt(options: Options, logicalModel: LogicalModel):LogicalModel
    fun codeModelBuilt(options: Options, codeModel: CodeModel):CodeModel
}
