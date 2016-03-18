class TokenInfo:

    """
    Stores information about the token
    """

    def __init__(self,token,idf = 0):
        """
        Constructor for TokenInfo
        :param token:  Stemmed Token
        :param idf: IDF value
        :return:
        """
        self.token = token
        self.idf = idf

    def __str__(self):
        """
        :return: String representation of the object
        """

        strToReturn = "{} {}".format(self.token, self.idf)
        return strToReturn

    def __repr__(self):
        """
        :return: returns string representation of the object
        """
        strToReturn = "TokenInfo({},{})".format(self.token, self.idf)
        return strToReturn

    def __hash__(self):
        """
        :return: returns hash value
        """

        return hash(self.token)

    def __eq__(self, other):
        """
        :param other: Check for equality
        :return:
        """
        return self.token == other.token

    def getToken(self):
        """
        :return: returns token
        """
        return self.token

    def getIDF(self):
        """
        :return: return IDF for the token
        """
        return self.idf

    def setIDF(self,idf):
        """
        For setting IDF value for the token
        :param idf:
        :return:
        """
        self.idf = idf
