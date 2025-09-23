PROBLEM STATEMENT:
Build a console app to manage products, bills of materials (BOM), work orders, 
material issues, production, and inventory updates.
Class Requirements
1. Product
2. BOM
3. BOMItem
4. WorkOrder
5. MaterialIssue
6. ProductionReport
7. Warehouse
Business Rules
1. Creating a work order reserves materials per BOM.
2. Materials must be issued before production reporting.
3. Finished goods increase stock; components decrease accordingly.
4. Work order status moves through defined stages.
5. Each material issue links to a work order and warehouse.
Console Interface Requirements
• Add Product / Define BOM / Create Work Order / Issue Materials / Report 
Production / Stock Summary / Exit
Expected Output Behavior
• Work order summary; issued material list; production yield; updated inventory.
Questions for Students:
1. Draw the UML Class Diagram for the system.
2. Implement the classes with the necessary data members and methods for 
system functionality and business rules.
3. Use encapsulation, inheritance, aggregation, and polymorphism wherever 
required.
4. Implement the main method for a menu-driven system.


SCREENSHOT OF UML DIAGRAM:
<img width="300" height="424" alt="image" src="https://github.com/user-attachments/assets/a5053673-c7c2-4b7a-95dd-69fd15654413" />
<img width="298" height="313" alt="image" src="https://github.com/user-attachments/assets/5d0ff0dc-cbcf-4021-8976-bdf593b210c7" />


HOW TO COMPILE AND RUN:
To compile the program, open a terminal or command prompt in the folder containing all the .java files and run javac *.java, 
which will compile every class in the project. Once the compilation is successful, start the application by running java Main,
which will launch the console menu where you can add products, define BOMs, create work orders, issue materials, report production, 
and view stock summaries.

OUTPUT SCREENSHORT:
<img width="662" height="758" alt="image" src="https://github.com/user-attachments/assets/61203c04-8c7b-4d51-b8f0-a35fcb08230e" />
<img width="317" height="369" alt="image" src="https://github.com/user-attachments/assets/5e4b87c1-0145-4992-a9f0-5448323298be" />



MALINI A
717823L129



