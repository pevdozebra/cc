db.createUser(
    {
        user    :   "sa",
        pwd     :   "sa",
        roles   :   [{
            role    :   "readWrite",
            db      :   "logging"
        }]
    }
)