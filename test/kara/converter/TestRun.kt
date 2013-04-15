package kara.converter
/**
* @author Stanislav Erokhin
*/


fun main(args : Array<String>) {
    println(KaraHTMLConverter.converter(
            """
    <!DOCTYPE html>
    <html>
        <head>
            <title>Bootstrap 101 Template</title>
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <!-- Bootstrap -->
            <link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
        </head>
        <body>
            <h1 class="main1">Hello, world!</h1>
            <div class="btn btn-info">
    Text
            Text text
          This is text
            </div>
            <script src="http://code.jquery.com/jquery.js"></script>
            <style>
                body {
                  color : red;
                }
            </style>
            <script src="js/bootstrap.min.js"></script>
        </body>
    </html>
            """, 0));

    println(KaraHTMLConverter.converter(
            """
    <p>
        <small>This line of text is meant to be treated as fine print.</small>
    </p>

    <strong>rendered as bold text</strong>

        <p class="text-left">Left aligned text.</p>
    <p class="text-center">Center aligned text.</p>
    <p class="text-right">Right aligned text.</p>


    <form>
    <fieldset>
    <legend>Legend</legend>
    <label>Label name</label>
    <input type="text" placeholder="Type something…">
    <span class="help-block">Example block-level help text here.</span>
    <label class="checkbox">
    <input type="checkbox"> Check me out
    </label>
    <button type="submit" class="btn">Submit</button>
    </fieldset>
    </form>
            """, 0));
}
