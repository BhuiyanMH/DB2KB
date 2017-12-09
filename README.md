Guideline to use SETL
General Information:
Development Language: Java.
Database Used: PostgreSQL version 9.6.

MAPPING:
Direct Mapping: Direct mapping can be used to convert all data form database to RDF without any customization. 
Steps:
1.	Click direct mapping button.
2.	Provide DB URL, DB Username and Password; then hit Connect button.
3.	Provide Base URL and hit Convert. Base URL must end with a ‘/’.
4.	RDF of the DB will be shown. 
5.	To save the RDF in a file, click on Save button and select Directory.

R2RML Mapping: R2RML mapping can be used to convert database data to RDF in a customized manner.
Steps:
1.	Click R2RML button.
2.	Provide DB URL, DB Username and Password; then hit Connect button.
3.	Provide Base URL. Base URL must end with a ‘/’.
4.	Name of all tables in the database is shown in the left of the window. 
5.	Click on a table name to customize. 
6.	Change the column properties as necessary and hit on Save Changes.
7.	Click on Proceed.
8.	Summary of the mapping will be shown. To change the mapping properties go to previous window by clicking Back button. Click Confirm to generate the R2RML mapping file. R2RML file is shown in the window.
9.	To immediately transform the data to RDF, click on Transform; RDF can be saved to file from there in N-triple format. 
10.	For further transformation, save the mapping file by clicking on the save button.


TRANSFORM:
CSV: This option can be used to convert CSV file data to its semantic RDF version in Turtle format. 
Steps:
1.	Click direct CSV button.
2.	Select a CSV file by hitting on Select button.
3.	Provide a base URL and click on Convert button.
4.	RDF equivalent of the CSV file will be shown in Turtle format; Hit Save to save in file.

RML Mapping: Transformation from previously generated or user defined RML file can be done using this menu. 
Steps:
1.	Click RML button.
2.	Provide DB URL, DB Username and Password; then hit Connect button.
3.	Load an RML file by clicking on Load button. 
4.	RDF generated form DB data using the provided mapping will be shown. 
5.	To edit the RDF, click on edit button.
6.	Click on Save button to save RDF in file.


TRANSFORM:
CSV: This option can be used to convert CSV file data to its semantic RDF version in Turtle format. 
Steps:
1.	Click direct CSV button.
2.	Select a CSV file by hitting on Select button.
3.	Provide a base URL and click on Convert button.
4.	RDF equivalent of the CSV file will be shown in Turtle format; Hit Save to save in file.

RETREIVE:
SPARQL Endpoint: This menu can be used to retrieve data from RDF file using SPARQL query.
Steps:
1.	Click SPQARQL Endpoint button.
2.	Load an RDF file by clicking on Load button. 
3.	Type SPARQL query in the query box and hit Run button. 
4.	The result of query will be shown in the window.

LINKING:
DBPedia: Our system can be used to link local data with DBpedia.
Steps:
1.	Open File button prompts the user to choose file and opens the RDF file.
2.	Extracts Objects From File button extracts the objects from RDF file.
3.	Select object Combo Box shows the extracted objects and allows user to select the desired object for matching.
4.	As soon as user selects an object from file, a list will appear which will allow user to select the desired properties for matching with DBpedia object. User can select multiple properties by holding ctrl button and with mouse click. Double click in the list item will allow the user to edit the property at one’s whim.
5.	View Graph From File button will show the selected properties and their values of the selected object from file graphically. 
6.	Select Key Property button will show the previously selected properties and allow the user to select a key property from the selected properties for fetching object from DBpedia.  Double click in the list item will allow the user to edit the property at one’s whim.
7.	Enter Max. Hits textbox will take any number from user and fetch that number of results from DBpedia.
8.	Fetch Data From DBpedia will fetch the results from DBpedia based on the key property’s value and max. Hit number.
9.	All results from DBpedia will appear in the dropdown list next to the Fetch Data From DBpedia button. When user selects an item from ;ist, the matching process between the selected DBpedia object and file object starts and the result will be shown in the screen. If the values don’t match, a message of “We are not sure about them” will be printed and if they match, a new property will be added to that object and will be showed at the end of matching process.
10.	View Graph From Web button will show the properties and their values graphically of the selected object from Web.


