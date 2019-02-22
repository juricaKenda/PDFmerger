# PDF Merger Application

A program that merges multiple .pdf files into one file. It presents you with an opportunity to choose the starting and the ending page for the merger. A very useful tool for "cutting out" unwanted pages from a particular pdf file.

## Getting Started

Everything required to use this application is contained within only two files. The first relevant file is the "Graphics" file which handles user input via GUI and outputs a .pdf file with desired pages. The second relevant file is the "PDFHandler" file. This file performs the requested operations on a specific .pdf file.

### Prerequisites

```
Apache PDFBox
See : https://pdfbox.apache.org/ to download the library.
```

```
Standard Java libraries.
```

### Installing

For use within IDE

```
Copy / download the file and run the Main within the Graphics class
```

For use as an application

```
Open the file in IDE, simply export it as a "Runnable JAR file".
```
## How to use the application ?
```
Run the application.
```
```
Select "Add PDF" Menu Item, and click on the first Add button.
```
```
When presented with a file chooser, select the desired .pdf file.
```
```
After you had selected the first file, click on the "Bound PDF" Menu Item.
```
```
Select the first Bound button.
```
```
When presented with a pop-up, type in the starting and the ending page for the merger, with a space between the two.
```
```
Repeat the process for n amount of .pdf files.
```
```
When all .pdf files had been selected and bound, click the button on the very bottom of the application named "Merge PDF Files".
```
```
When presented with a location chooser, select the desired location of the merged file and name it "xyz.pdf".
```
```
If the merger was successful, the application will output the file location on the display.
```

## Authors

* **Jurica Kenda** - *Initial work* - [Jurica Kenda](https://github.com/juricaKenda)


## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details