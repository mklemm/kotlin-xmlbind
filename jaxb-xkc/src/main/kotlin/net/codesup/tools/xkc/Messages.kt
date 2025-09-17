package net.codesup.tools.xkc

import java.text.MessageFormat
import java.util.*


/**
 * Formats error messages.
 */
object Messages {
    /** Loads a string resource and formats it with specified arguments.  */
    fun format(property: String?, vararg args: Any?): String {
        val text =
            ResourceBundle.getBundle(Messages::class.java.getPackage().name + ".MessageBundle").getString(property)
        return MessageFormat.format(text, *args)
    }

    //
    //
    // Message resources
    //
    //
    const val UNKNOWN_LOCATION =  // 0 args
        "ConsoleErrorReporter.UnknownLocation"
    const val LINE_X_OF_Y =  // 2 args
        "ConsoleErrorReporter.LineXOfY"
    const val UNKNOWN_FILE =  // 0 args
        "ConsoleErrorReporter.UnknownFile"
    const val DRIVER_PUBLIC_USAGE =  // 0 args
        "Driver.Public.Usage"
    const val DRIVER_PRIVATE_USAGE =  // 0 args
        "Driver.Private.Usage"
    const val ADDON_USAGE =  // 0 args
        "Driver.AddonUsage"
    const val EXPERIMENTAL_LANGUAGE_WARNING =  // 2 arg
        "Driver.ExperimentalLanguageWarning"
    const val NON_EXISTENT_DIR =  // 1 arg
        "Driver.NonExistentDir"

    // Usage not found. TODO Remove
    // static final String MISSING_RUNTIME_PACKAGENAME = // 0 args
    //     "Driver.MissingRuntimePackageName";
    const val MISSING_MODE_OPERAND =  // 0 args
        "Driver.MissingModeOperand"

    // Usage not found. TODO Remove
    // static final String MISSING_COMPATIBILITY_OPERAND = // 0 args
    //     "Driver.MissingCompatibilityOperand";
    const val INVALID_JAVA_MODULE_NAME =  // 1 arg
        "Driver.INVALID_JAVA_MODULE_NAME"
    const val MISSING_PROXY =  // 0 args
        "Driver.MISSING_PROXY"
    const val MISSING_PROXYFILE =  // 0 args
        "Driver.MISSING_PROXYFILE"
    const val NO_SUCH_FILE =  // 1 arg
        "Driver.NO_SUCH_FILE"
    const val ILLEGAL_PROXY =  // 1 arg
        "Driver.ILLEGAL_PROXY"
    const val ILLEGAL_TARGET_VERSION =  // 1 arg
        "Driver.ILLEGAL_TARGET_VERSION"
    const val MISSING_OPERAND =  // 1 arg
        "Driver.MissingOperand"
    const val MISSING_PROXYHOST =  // 0 args
        "Driver.MissingProxyHost"
    const val MISSING_PROXYPORT =  // 0 args
        "Driver.MissingProxyPort"
    const val STACK_OVERFLOW =  // 0 arg
        "Driver.StackOverflow"
    const val UNRECOGNIZED_MODE =  // 1 arg
        "Driver.UnrecognizedMode"
    const val UNRECOGNIZED_PARAMETER =  // 1 arg
        "Driver.UnrecognizedParameter"
    const val UNSUPPORTED_ENCODING =  // 1 arg
        "Driver.UnsupportedEncoding"
    const val MISSING_GRAMMAR =  // 0 args
        "Driver.MissingGrammar"
    const val PARSING_SCHEMA =  // 0 args
        "Driver.ParsingSchema"
    const val PARSE_FAILED =  // 0 args
        "Driver.ParseFailed"
    const val COMPILING_SCHEMA =  // 0 args
        "Driver.CompilingSchema"
    const val FAILED_TO_GENERATE_CODE =  // 0 args
        "Driver.FailedToGenerateCode"
    const val FILE_PROLOG_COMMENT =  // 1 arg
        "Driver.FilePrologComment"
    const val DATE_FORMAT =  // 0 args
        "Driver.DateFormat"
    const val TIME_FORMAT =  // 0 args
        "Driver.TimeFormat"
    const val AT =  // 0 args
        "Driver.At"
    const val VERSION =  // 0 args
        "Driver.Version"
    const val FULLVERSION =  // 0 args
        "Driver.FullVersion"
    const val BUILD_ID =  // 0 args
        "Driver.BuildID"
    const val ERROR_MSG =  // 1:arg
        "Driver.ErrorMessage"
    const val WARNING_MSG =  // 1:arg
        "Driver.WarningMessage"
    const val INFO_MSG =  // 1:arg
        "Driver.InfoMessage"
    const val ERR_NOT_A_BINDING_FILE =  // 2 arg
        "Driver.NotABindingFile"
    const val ERR_TOO_MANY_SCHEMA =  // 0 args
        "ModelLoader.TooManySchema"
    const val ERR_BINDING_FILE_NOT_SUPPORTED_FOR_RNC =  // 0 args
        "ModelLoader.BindingFileNotSupportedForRNC"
    const val DEFAULT_VERSION =  // 0 args
        "Driver.DefaultVersion"
    const val DEFAULT_PACKAGE_WARNING =  // 0 args
        "Driver.DefaultPackageWarning"
    const val NOT_A_VALID_FILENAME =  // 2 args
        "Driver.NotAValidFileName"
    const val FAILED_TO_PARSE =  // 2 args
        "Driver.FailedToParse"
    const val NOT_A_FILE_NOR_URL =  // 1 arg
        "Driver.NotAFileNorURL"
    const val FIELD_RENDERER_CONFLICT =  // 2 args
        "FIELD_RENDERER_CONFLICT"
    const val NAME_CONVERTER_CONFLICT =  // 2 args
        "NAME_CONVERTER_CONFLICT"
    const val FAILED_TO_LOAD =  // 2 args
        "FAILED_TO_LOAD"
    const val PLUGIN_LOAD_FAILURE =  // 1 arg
        "PLUGIN_LOAD_FAILURE"
}
