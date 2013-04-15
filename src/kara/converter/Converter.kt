package kara.converter
/**
* @author Stanislav Erokhin
*/

import java.util.HashMap
import org.jsoup.select.NodeVisitor
import org.jsoup.nodes.Node
import org.jsoup.Jsoup
import org.jsoup.select.NodeTraversor
import kara.converter.NodeType.*
import org.jsoup.nodes.Attributes

enum class NodeType {
    document
    doctype
    text
    comment
    data
    element
}

public object KaraHTMLConverter {
    val DEPTH_SPACE_COUNT = 3



    private fun getNodeType(node : Node): NodeType {
        return when (node.nodeName()) {
            "#document" -> document
            "#doctype" -> doctype
            "#text" -> text
            "#comment" -> comment
            "#data" -> data
            else -> element
        }
    }

    private fun hasBodyTag(htmlText : String): Boolean {
        return htmlText.contains("<body ")
    }

    public fun converter(htmlText : String, startDepth : Int = 0): String {
        val str = StringBuilder()
        if (hasBodyTag(htmlText)) {
            val doc = Jsoup.parse(htmlText)
            NodeTraversor(KaraConvertNodeVisitor(str, startDepth - 1)).traverse(doc)         // -1 because root node is #document
        } else {
            val doc = Jsoup.parseBodyFragment(htmlText)
            for (element in doc!!.body()!!.childNodes()!!) {
                NodeTraversor(KaraConvertNodeVisitor(str, startDepth)).traverse(element)
            }
        }
        return str.toString()
    }


    private fun styleClassConvert(styleClass : String): String {
        return styleClass.replace('-', '_')
    }

    /**
        styleAttr = "main1 btn-info"
        @return  main1 + btn_info
    */
    fun styleClasses(styleAttr : String): String {
        val classes = styleAttr.split(' ')
        val str = StringBuilder()
        for (styleClass in classes) {
            if (str.length() != 0) {
                str.append(" + ")
            }
            str.append(styleClassConvert(styleClass))
        }

        return str.toString()
    }

    private fun spaces(depth : Int) : String {
        return " ".repeat(depth * DEPTH_SPACE_COUNT)
    }

    private fun dataConverter(text : String, depth :  Int): String {
        val trimText = text.trim()
        if (trimText.isEmpty()) return ""

        val lines = trimText.split('\n')
        val str = StringBuilder()
        for (line in lines) {
            str.append(spaces(depth)).append(line.trim()).append('\n')
        }
        return str.toString()
    }

    private fun textConverter(text : String, depth :  Int): String {
        val trimText = text.trim()
        if (trimText.isEmpty()) return ""

        val lines = trimText.split('\n')
        val str = StringBuilder()
        for (line in lines) {
            str.append(spaces(depth)).append("+\"").append(line.trim()).append("\"\n")
        }
        return str.toString()
    }

    private fun attributesConverter(attributes : Attributes): String {
        val str = StringBuilder()
        for (attr in attributes.asList()!!) {
            if (str.length() > 0) {
                str.append(", ")
            }
            if (attr.getKey().equals("class")) {
                str.append("c = ").append(styleClasses(attr.getValue()))
                continue
            }
            str.append(attr.getKey()).append(" = \"").append(attr.getValue()).append("\"")
        }
        return str.toString()
    }


    private class KaraConvertNodeVisitor(val stringBuilder : StringBuilder, val startDepth : Int) : NodeVisitor {

        public override fun head(node: Node?, depth: Int) {
            val realDepth = depth + startDepth
            when (getNodeType(node!!)) {
                document, doctype -> {}
                text -> {
                    val convertedText = textConverter(node.attr("text")!!, realDepth)
                    if (!convertedText.isEmpty()) stringBuilder.append(convertedText)
                }

                comment -> stringBuilder.append(spaces(realDepth)).append("/*\n")
                        .append(dataConverter(node.attr("comment")!!, realDepth + 1))

                data -> stringBuilder.append(spaces(realDepth)).append("\"\"\"\n")
                        .append(dataConverter(node.attr("data")!!, realDepth + 1))

                element -> {
                    stringBuilder.append(spaces(realDepth)).append(node.nodeName())
                    val attrStr = attributesConverter(node.attributes()!!)
                    if (!attrStr.isEmpty()) stringBuilder.append('(').append(attrStr).append(')')

                    if (node.childNodeSize() != 0) {
                        stringBuilder.append(" {\n")
                    } else {
                        stringBuilder.append("\n")
                    }
                }

                else -> throw IllegalStateException()
            }
        }

        public override fun tail(node: Node?, depth: Int) {
            val realDepth = depth + startDepth
            when (getNodeType(node!!)) {
                document, doctype -> {}
                text -> {}
                comment -> stringBuilder.append(spaces(realDepth)).append("*/\n")
                data -> stringBuilder.append(spaces(realDepth)).append("\"\"\"\n")
                element -> {
                    if (node.childNodeSize() != 0) stringBuilder.append(spaces(realDepth)).append("}\n")
                }

                else -> throw IllegalStateException()
            }
        }
    }

}





