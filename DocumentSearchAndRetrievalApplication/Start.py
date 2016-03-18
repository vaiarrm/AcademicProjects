import os
from IIREngine import Engine

engine = Engine()
def forFirstRun(docPath):
    """
    Calls Engine.createInvertedIndex to generate term indexes
    :param docPath:
    :return:
    """
    print("Please Wait...Creating Index for First Use...This Might Take Time...")
    engine.createInvertedIndexForFirstUse(docPath)
    print("Index Created")

def forSubsequentRun():
    """
    Calls Engine. to generate term indexes from stored files

    :return:
    """
    print("Please Wait...Creating Index from Previous Run...This Might Take Few Minutes...")
    engine.createInvertedIndexForSubsequentUse()
    print("Index Created")



def indexCreation():
    """
    Creates index for first and subsequent times
    :return:
    """
    if engine.checkIfIndexPreviouslyCreated():
        #create index from file
        forSubsequentRun()
        pass
    else:
        #create index for first time use
        while True:
            docPath = input("This is the firt run...Please enter the path where documents are stored for which index needs to be created")
            docPath = docPath.strip()
            if len(docPath) == 0:
                print("Incorrect Document Path - Document Path Length is Zero")
            else:
                try:
                    os.listdir(docPath)
                    break
                except:
                    print("Incorrect Document Path - Cannot Access Docment Path")

        forFirstRun(docPath)


def queryProcessor(query):
    """
    Processes query by calling engine.queryProcessor
    :param query:
    :return:
    """
    return engine.queryProcessor(query)

def start():
    """
    Main UI of the Applicaiton
    :return:
    """
    indexCreation()
    while True:
        query = input("Please enter the query: ")
        resultList = queryProcessor(query)
        print("No Of Documents Retrieved {}".format(len(resultList)))
        for item in resultList:
            print(item)
        option = input(" Do You Want to Continue? Y/N")
        if option.lower().strip() == "n":
            break
    print("Exiting...Good Bye!!!")


