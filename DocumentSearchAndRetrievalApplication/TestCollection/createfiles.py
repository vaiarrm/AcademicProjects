def createFiles(filePath):
              infile = open(filePath)
              content = infile.read()
              infile.close()

              lst = content.split("*")

              for i in range(1,len(lst)-1):
                            s = lst[i].split()
                            if len(s) < 5:
                                          continue
                            fileName = s[1]+".txt"
                            infile = open(fileName,"w")
                            con = " "
                            con = con.join(s[5:])
                            infile.write(con)
                            infile.close()

              
createFiles("/Users/vaibhavsharma/Dropbox/DePaul/CSC575/Project/testCollection/Query/TIMEALL.txt")             
