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
}
