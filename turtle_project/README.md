# assignment3

A Quil sketch designed to show turtle graphics.

Function to start running the sample turtle program - start-program.

Known issues:
1. Sometimes the file gets read more than once while running and so the instructions will be populated more than once. To avoid this I have a file-read atom which is set to 1 on the 1st file read.
2. When undoing the 1st command to display nothing on screen in the turtle program it is a bit slow, for undoing higher commands other than the 1st it works fast.

## Usage

LightTable - open `core.clj` and press `Ctrl+Shift+Enter` to evaluate the file.

Emacs - run cider, open `core.clj` and press `C-c C-k` to evaluate the file.

REPL - run `(require 'assignment3.core)`.

## License

Copyright © 2014 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
