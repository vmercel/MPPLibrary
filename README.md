# Library System Application

## Description
The Library System Application is a comprehensive software solution designed to manage the functionalities of a library. It allows administrators and librarians to efficiently manage members, books, checkouts, and overdue items, ensuring a smooth library operation.

## Features
- **User Login**
- **Add Member**
- **Show Members**
- **Add Book**
- **Add Book Copy**
- **Show Books**
- **Checkout**
- **Overdue Books**
- **Check In**

## Table of Contents
- [User Login](#user-login)
- [Add Member](#add-member)
- [Show Members](#show-members)
- [Add Book](#add-book)
- [Add Book Copy](#add-book-copy)
- [Show Books](#show-books)
- [Checkout](#checkout)
- [Overdue Books](#overdue-books)
- [Check In](#check-in)

## User Login
The login feature allows users to securely access the system using their credentials. Upon successful login, users can perform various operations based on their roles (Administrator or Librarian).

![User Login Use Case](docs/login%20USECASE.png)

![User Login Sequence](docs/User%20Login%20Sequence.png)

## Add Member
Administrators can add new members to the library system. This feature collects necessary information from users, such as name, address, and contact details.

![Add Member Use Case](docs/add%20member%20USECASE.png)

![Add Library Member Sequence](docs/Add%20Library%20Member%20Sequence.png)

## Show Members
This feature displays a list of all library members, allowing users to search and view member details.

## Add Book
Librarians can add new books to the system, providing essential details such as title, authors, ISBN, and publication year.

![Add Book Use Case](docs/add%20book%20USECASE.png)

![Add New Book Sequence](docs/Add%20New%20Book%20Sequence.png)

## Add Book Copy
This functionality allows librarians to add multiple copies of existing books, facilitating better inventory management.

![Add Book Copy Use Case](docs/add%20book%20copy%20USECASE.png)

![Add Book Copy Sequence](docs/Add%20Book%20Copy%20Sequence.png)

## Show Books
Users can view a list of all books available in the library, complete with details such as title, author, and available copies.

## Checkout
Library members can check out books, with the system tracking due dates and lending history.

![Check Out Use Case](docs/check%20out%20USECASE.png)

![Book Checkout Sequence](docs/Book%20Checkout%20Sequence.png)

## Overdue Books
This feature displays a list of overdue books, allowing librarians to manage penalties and notifications for members.

![Overdue Use Case](docs/overdue%20USECASE.png)

![Check Overdue Panel Sequence](docs/Check%20Overdue%20Panel%20Sequence.png)

## Check In
Members can return books to the library, with the system updating the status and availability of the returned books.

![Check In Use Case](docs/check%20in%20USECASE.png)

![Book Check-In Sequence](docs/Book%20Check-In%20Sequence.png)

## Diagrams
The following diagrams provide a visual representation of the system architecture and functionality:
- Class Diagram: ![Class Diagram 1](docs/class%20diag%201.png)
- Class Diagram: ![Class Diagram](docs/CLASS%20DIAG.png)

## Additional Files
- **`filenames.py`**: A script for generating file lists.
- **`file_list.txt`**: Output from `filenames.py`, listing files in the current directory.

## Conclusion
The Library System Application is designed to streamline library management and enhance user experience. Its robust features support efficient handling of members and books, ensuring the smooth operation of library services.
