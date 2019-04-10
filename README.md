[Tablevert]: http://www.tablevert.org "Tablevert"
[`tablevert-core`]: https://github.com/tablevert/tablevert-core "tablevert-core"
[`tablevert-service`]: https://github.com/tablevert/tablevert-service "tablevert-service"

# Tablevert

[Tablevert] is a toolset for the conversion of data tables from one format to another. 

Imagine your data is stored safely in a database. Now your product owner demands that filtered and sorted subsets of the records be displayed as a table in his web UI. Beneath that table, a nice little button is expected to offer an XLSX file download of the data.

All Tablevert access to source tables is read-only. In this context, a _table_ does not necessarily mean a database table, but rather any view or formula which results in tabular data, e.g. a `SELECT` statement.

The following modules are offered:

* [`tablevert-core`] is a Java library containing the actual conversion logic.

* [`tablevert-service`] is a Spring Boot application offering Tablevert functionality as a REST API 

# tablevert-core

[`tablevert-core`] is a Java library containing the core [Tablevert] functionality. 

A more detailed description will follow.


