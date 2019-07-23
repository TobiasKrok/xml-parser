# xml-parser
This is a rewrite of an old program I wrote (or helped write, needed help with it) in C#. It reads ViewActions.xml and finds missing key/value pairs in the file.

If it finds any correct key/value pairs, it will output it to a file called ci_web.properties. It is created during execution.
If it finds any missing key/value pairs, it will output the whole XML node to an errorlog called ci_web_errorlog.txt. 


