package kara.converter
/**
* @author Stanislav Erokhin
*/

import java.util.HashMap
import org.jsoup.select.NodeVisitor
import org.jsoup.nodes.Node
import org.jsoup.Jsoup
import org.jsoup.select.NodeTraversor


object KaraHTMLConverter {
    val renameParamMap : Map<String, String> = HashMap<String, String>()



    fun converter(htmlText : String, startDepth : Int): String {
        val str = StringBuilder()
        val doc = Jsoup.parse(htmlText);
        NodeTraversor(KaraConvertNodeVisitor(0)).traverse(doc)

        return str.toString()
    }

    class KaraConvertNodeVisitor(val startDepth : Int) : NodeVisitor {

        public override fun head(node: Node?, depth: Int) {
            println(node!!.nodeName())
        }

        public override fun tail(node: Node?, depth: Int) {
            println(node!!.nodeName())
        }
    }

}





