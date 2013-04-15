package kara.converter
/**
* @author Stanislav Erokhin
*/

import java.util.HashMap
import org.jsoup.select.NodeVisitor
import org.jsoup.nodes.Node
import org.jsoup.Jsoup
import org.jsoup.select.NodeTraversor
import org.jsoup.nodes.Attributes



object KaraHTMLConverter {
    val DEPTH_SPACE_COUNT = 3

    enum class NodeType {
        document
        doctype
        text
        comment
        data
        element
    }

    private fun getNodeType(node : Node): NodeType {
        val name = node.nodeName()!!
        when {
            name.equals("#document") -> return NodeType.document
            name.equals("#doctype") -> return NodeType.doctype
            name.equals("#text") -> return NodeType.text
            name.equals("#comment") -> return NodeType.comment
            name.equals("#data") -> return NodeType.data
            else -> return NodeType.element
        }
    }

    fun converter(htmlText : String, startDepth : Int = 0): String {
        val str = StringBuilder()
        val doc = Jsoup.parse(htmlText)
        NodeTraversor(KaraConvertNodeVisitor(str, 0)).traverse(doc)
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

    private fun textConverter(text : String, depth :  Int): String {
        return text; //TODO:
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

    private fun tagConverter(tagNode : Node): String {
        return ""
    }

    class KaraConvertNodeVisitor(val stringBuilder : StringBuilder, val startDepth : Int) : NodeVisitor {

        public override fun head(node: Node?, depth: Int) {
            val realDepth = depth + startDepth
            when (getNodeType(node!!)) {
                NodeType.document, NodeType.doctype -> {}
                NodeType.text -> stringBuilder.append(textConverter(node.attr("text")!!, realDepth))

                NodeType.comment -> stringBuilder.append(spaces(realDepth)).append("/*\n")
                        .append(textConverter(node.attr("comment")!!, realDepth))

                NodeType.data -> stringBuilder.append(textConverter(node.attr("data")!!, realDepth))

                NodeType.element -> {
                    stringBuilder.append(spaces(realDepth)).append(node.nodeName())
                    val attrStr = attributesConverter(node.attributes()!!)
                    if (!attrStr.isEmpty()) stringBuilder.append('(').append(attrStr).append(')')
                    stringBuilder.append(" {\n")
                }

                else -> throw IllegalStateException()
            }
        }

        public override fun tail(node: Node?, depth: Int) {
            val realDepth = depth + startDepth
            when (getNodeType(node!!)) {
                NodeType.document, NodeType.doctype -> {}
                NodeType.text -> stringBuilder.append("\n")

                NodeType.comment -> stringBuilder.append(spaces(realDepth)).append("\n*/\n")

                NodeType.data -> stringBuilder.append("\n")
                NodeType.element -> stringBuilder.append(spaces(realDepth)).append("}\n")

                else -> throw IllegalStateException()
            }
        }
    }

}





