package kara.converter
import org.junit.Test as test
import org.junit.Assert.*
/**
* @author Stanislav Erokhin
*/


public class SimpleTest {

    fun runTest(inp : String, out : String) {
        assertEquals(out.trim() + "\n", KaraHTMLConverter.converter(inp))
    }


    test fun simple() {
        runTest(
                """
                <div> text</div>
                """,

                """
div {
    +"text"
}
                """

        )
    }
}