import os
from IIREngine import Engine
from Start import forSubsequentRun, forFirstRun, queryProcessor

engine = Engine()

def startTest(queryFile, docPath=None):
    """
    Runs the batch test cases using the batch file

    :param queryFile: File which has all the queries to be processed
    :param docPath: If it is the first run docPath is required
    :return:
    """


    if engine.checkIfIndexPreviouslyCreated():
        print("Creating Index...")
        forSubsequentRun()
    else:
        if docPath == None:
            docPath = input("Please enter docPath for first run")
        while True:
            if len(docPath) == 0:
                print("Incorrect Document Path - Document Path Length is Zero")
            else:
                try:
                    os.listdir(docPath)
                    print("Creating Index...")
                    forFirstRun(docPath)
                    break
                except:
                    print("Incorrect Document Path - Cannot Access Docment Path")

    # Processing Query File
    try:
        queryFile = queryFile.strip()
        infile = open(queryFile, "r")
        contents = infile.readlines()
        infile.close()
        queryNo = 1
        try:
            testOutFile = open("testOutFile.txt","w")
        except:
            print("error opening testOutFile.txt")
        for line in contents:
            query = line.lower().strip()
            print("Processing Query {} - {}".format(queryNo , query))
            returnList = queryProcessor(query)
            strToPrint1 = "Result for query {} {}".format(queryNo , query)
            try:
                testOutFile.write(strToPrint1)
                testOutFile.write("\n")
            except:
                print("error writing testOutFile - Info")
            for item in returnList:
                strToPrint2 = " " * 5 + item
                try:
                    testOutFile.write(strToPrint2)
                    testOutFile.write("\n")
                except:
                    print("error writing testOutFile - query results")
            queryNo += 1
        testOutFile.close()
    except:
        print("Error Opening Query File")
        return
    print("Exiting...")
