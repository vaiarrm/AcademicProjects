class DocumentReference:
    """
    This class is for storing information about the document
    """

    def __init__(self,docName,absolutePath,docLength = 0.0):
        """
        Constructor for DocumentReference class

        :param docName: Document Name
        :param absolutePath: Absolute Path
        :param docLength: Length of the Document
        :return:
        """
        self.docName = docName
        self.absolutePath = absolutePath
        self.docLength = docLength

    def __str__(self):
        """
        returns string description of the object
        :return:
        """
        strToReturn = "{} {} {}".format(self.docName, self.absolutePath,self.docLength)
        return strToReturn

    def __repr__(self):
        """
            Returns string representation of the object
        :return:
        """
        strToReturn = "DocumentReference({},{},{})".format(self.docName, self.absolutePath,self.docLength)
        return strToReturn

    def getAbsolutePath(self):
        """
        return absolute path

        :return:
        """
        return self.absolutePath

    def getDocName(self):
        """
        Returns document Name
        :return:
        """
        return self.docName

    def getDocLength(self):
        """
        Returns Document Length
        :return:
        """
        return self.docLength

    def setDocLength(self,docLength):
        """
        Method for Setting Document Length
        :param docLength:
        :return:
        """
        self.docLength = docLength
        
