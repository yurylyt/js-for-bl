importClass(Packages.java.lang.System)
function getStatusString(status) {
    var statuses = {1: "NEW", 2: "IN PROGRESS", 3: "CLOSED"};
    System.out.println("blah!");
    return statuses[status] ? statuses[status] : null;
}
