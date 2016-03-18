import os
import math

from DocumentRefernce import DocumentReference
from nltk.stem import PorterStemmer
from TokenInfo import TokenInfo
from constants import punctuations, stopwords, docDictionaryFile, tokenDictionaryFile, dictionaryFile
from operator import itemgetter

class Engine:
    def __init__(self):
        """
        Constructor for Engine
        :return:
        """
        self.docDictionary = {}
        self.tokensDictionary = {}
        self.dictionary = {}

    def cleanPunctuation(self,content):
        """
        To Clean data
        :param content: Data to be cleaned
        :return:
        """
        return content.translate(str.maketrans(punctuations," "*len(punctuations)))

    def crawler(self,path):
        """
        Crawl directory to find all the documents
        :param path:
        :return:
        """
        docList = []
        docList = self.crawlerHelper(path,docList)
        docDictionary= {}
        for doc in docList:
            docDictionary[doc.getAbsolutePath()] = doc
        return docDictionary

    def crawlerHelper(self,path,docList):
        """
        Helper for crawler
        :param path:
        :param docList:
        :return:
        """
        for item in os.listdir(path):
            if item[0] != ".":
                absolutePath = os.path.join(path,item)
                if os.path.isdir(absolutePath):
                    docList = self.crawlerHelper(absolutePath,docList)
                else:
                    doc = DocumentReference(item,absolutePath)
                    docList.append(doc)
        return docList

    def stemWord(self,word):
        """
        Stems the word using Porter's Algorithm
        :param word: To Be Stemmed
        :return:
        """
        stemmer=PorterStemmer()
        return stemmer.stem(word)

    def isStopWord(self,word):
        """
        Checks if the word is a stop word or not
        :param word: True if it is a stop word otherwise False
        :return:
        """
        return word in stopwords

    def createInvertedIndexForFirstUse(self,startPath):
        """
        Update values of dictionary for indexing. This will be called for first run
        :param startPath:
        :return:
        """
        self.docDictionary = self.crawler(startPath)
        if len(self.docDictionary) == 0:
            print("No Documents In Given Folder")
            return
        #create Dictionary
        # {Token : {Document Absolute Path : count }}
        for docAbsPath in self.docDictionary:
            try:
                infile = open(docAbsPath,"r")
                content = infile.read()
                infile.close()

                content = self.cleanPunctuation(content) # Removing Punctuations
                splitContent = content.split() # Creating List of Words for Processing

                for word in splitContent:
                    word = word.lower().strip()

                    # Stop Word Check
                    if self.isStopWord(word):
                        continue

                    # Stemming the word using Porter's Algorithm
                    token =  self.stemWord(word)
                    if token not in self.tokensDictionary:
                        self.tokensDictionary[token] = TokenInfo(token)
                    if token not in self.dictionary:
                        # Token not in dictionary yet
                        self.dictionary[token] = {docAbsPath:1}
                    else:
                        # Token in dictionary
                        if docAbsPath in self.dictionary[token]:
                            # Token Already encountered in the document
                            self.dictionary[token][docAbsPath] += 1
                        else:
                            # Token not encountered in the document
                            self.dictionary[token][docAbsPath] = 1
            except:
                print("Error Processing File {}".format(docAbsPath))
                continue

        # Set IDF values
        noOfDocs = len(self.docDictionary)
        for token in self.tokensDictionary:
            docFrequency = len(self.dictionary[token])
            self.tokensDictionary[token].setIDF(math.log(noOfDocs/docFrequency,2))

        #Calcuate Length
        for token in self.tokensDictionary:
            tokenIDF = self.tokensDictionary[token].getIDF()
            occurence = self.dictionary[token]
            for docAbsPath in occurence:
                termFrequencyInDoc = occurence[docAbsPath]
                prevLen = self.docDictionary[docAbsPath].getDocLength()
                self.docDictionary[docAbsPath].setDocLength(((tokenIDF * termFrequencyInDoc) ** 2)+prevLen)

        for docAbsPath in self.docDictionary:
            self.docDictionary[docAbsPath].setDocLength(math.sqrt(self.docDictionary[docAbsPath].getDocLength()))

        #Store Indexes in File
        docFile = open(docDictionaryFile,"w")
        for docAbsPath in self.docDictionary:
            doc = self.docDictionary[docAbsPath]
            strToWrite = "{} {} {}".format(docAbsPath,doc.getDocName(),doc.getDocLength())
            docFile.write(strToWrite)
            docFile.write("\n")
        docFile.close()

        tokenFile = open(tokenDictionaryFile,"w")
        for token in self.tokensDictionary:
            tokenInfo = self.tokensDictionary[token]
            strToWrite = "{} {}".format(token,tokenInfo.getIDF())
            tokenFile.write(strToWrite)
            tokenFile.write("\n")
        tokenFile.close()

        dicFile = open(dictionaryFile,"w")
        for token in self.dictionary:
            occurence = self.dictionary[token]
            for docAbsPath in occurence:
                strToWrite  = "{} {} {}".format(token, docAbsPath, occurence[docAbsPath])
                dicFile.write(strToWrite)
                dicFile.write("\n")
        dicFile.close()

        # print(self.dictionary)
        # print(self.docDictionary)
        # print(self.tokensDictionary)

    def createInvertedIndexForSubsequentUse(self):
        """
        Update values of dictionary for indexing. This will be called for subsequent runs
        :return:
        """

        docFile = open(docDictionaryFile,"r")
        content = docFile.readlines()
        docFile.close()
        for line in content:
            line = line.strip()
            if len(line) == 0:
                continue
            lineSplit = line.split()
            docAbsPath = lineSplit[0]
            docName = lineSplit[1]
            docLength = eval(lineSplit[2])
            self.docDictionary[docAbsPath] = DocumentReference(docName,docAbsPath,docLength)

        dicFile = open(dictionaryFile,"r")
        content = dicFile.readlines()
        dicFile.close()

        for line in content:
            line = line.strip()
            if len(line) == 0:
                continue
            lineSplit = line.split()
            token = lineSplit[0]
            docAbsPath = lineSplit[1]
            count = eval(lineSplit[2])
            if token not in self.dictionary:
                self.dictionary[token]  = {docAbsPath:count}
            else:
                self.dictionary[token][docAbsPath] = count

        tokenFile = open(tokenDictionaryFile,"r")
        content = tokenFile.readlines()
        tokenFile.close()

        for line in content:
            line = line.strip()
            if len(line) == 0:
                continue
            lineSplit = line.split()
            token = lineSplit[0]
            IDF = eval(lineSplit[1])
            tokenInfo = TokenInfo(token,IDF)
            self.tokensDictionary[token] = tokenInfo



    def checkIfIndexPreviouslyCreated(self):
        """
        :return: True if there was a previous run else returns false
        """
        try:
            infile1 = open(docDictionaryFile,"r")
            infile1.close()
            infile2 = open(tokenDictionaryFile,"r")
            infile2.close()
            return True
        except:
            return False

    def queryProcessor(self,query):
        """
        This method processes the query and retireves documents

        :param query: Query To Be Processed
        :return: List of Retrieved Docuements
        """
        query = self.cleanPunctuation(query)
        splitQuery = query.split()
        queryDic = {}
        for word in splitQuery:
            word = word.lower().strip()
            # Stop Word Check
            if self.isStopWord(word):
                continue
            # Stemming the word using Porter's Algorithm
            token =  self.stemWord(word)
            if token in self.tokensDictionary:
                if token in queryDic:
                    queryDic[token] +=1
                else:
                    queryDic[token] = 1
        retrievedDocs = {}
        for token in queryDic:
            IDF = self.tokensDictionary[token].getIDF()
            termFrequencyInQuery = queryDic[token]
            weight = IDF * termFrequencyInQuery
            queryDic[token] = weight
            #determing query length

        queryLength = 0
        for token in queryDic:
            queryLength += queryDic[token] ** 2
        queryLength = math.sqrt(queryLength)

        for token in queryDic:
            occurence = self.dictionary[token]
            weight = queryDic[token]
            IDF = self.tokensDictionary[token].getIDF()

            for docAbsPath in occurence:
                count = occurence[docAbsPath]
                if docAbsPath not in retrievedDocs:
                    retrievedDocs[docAbsPath] = weight * IDF * count
                else:
                    retrievedDocs[docAbsPath] += weight * IDF * count

        retrievedDocsList = []
        for docAbsPath in retrievedDocs:
            retrievedDocs[docAbsPath] = (retrievedDocs[docAbsPath])/(queryLength * self.docDictionary[docAbsPath].getDocLength())
            retrievedDocsList.append((docAbsPath,retrievedDocs[docAbsPath]))

        retrievedDocsListSorted = sorted(retrievedDocsList,key=itemgetter(1))
        retrievedDocsListSorted.reverse()
        returnLst = []
        for item in retrievedDocsListSorted:
            returnLst.append(item[0])
        return returnLst

















































