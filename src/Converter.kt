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
            stringBuilder.append(spaces(depth + startDepth) + node!!.nodeName() + "(" +
            attributesConverter(node.attributes()!!) + ") {\n")
        }

        public override fun tail(node: Node?, depth: Int) {
            stringBuilder.append(spaces(depth + startDepth) + "}\n")
        }
    }

}





