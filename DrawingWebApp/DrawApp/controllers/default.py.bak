# -*- coding: utf-8 -*-
# this file is released under public domain and you can use without limitations

#########################################################################
## This is a sample controller
## - index is the default action of any application
## - user is required for authentication and authorization
## - download is for downloading files uploaded in the db (does streaming)
#########################################################################

def index():
    imageList = db(db.Image.ImageViewPermission == True).select(db.Image.id,db.Image.ImageTitle,db.Image.created_by,db.Image.created_on,orderby=~db.Image.created_on | db.Image.ImageTitle)
    return dict(imageList = imageList)

@auth.requires_login()
def myImages():
    fname =db(db.Image.created_by==auth.user_id).select().first()
    imageList = db(db.Image.created_by == auth.user.id).select(db.Image.id,db.Image.ImageTitle,db.Image.created_by,db.Image.created_on,orderby=~db.Image.created_on | db.Image.ImageTitle)
    return dict(imageList=imageList,fname=fname)

@auth.requires_login()
def uploadImages():
    form = SQLFORM(db.Image,labels={'ImageTitle':'Image Title:','ImageUpload':'Image:','ImageViewPermission':'Share Image'}).process()
    if form.accepted:
        session.flash = "Image Uploaded"
        redirect(URL('myImages'))
    return dict(form=form)
@auth.requires_login()
def drawNewImage():
    if request.vars.imageFormTitle:
        imageBlob = request.vars.imageFormBlob
        imageTitle = request.vars.imageFormTitle
        if request.vars.imageFormPermission == "True":
            imagePermission = True
        else:
            imagePermission= False
        db.Image.insert(ImageTitle = imageTitle,ImageCanvas=imageBlob,ImageViewPermission = imagePermission)
        redirect(URL('myImages'))
    return dict()
@auth.requires_login()
def editImage():
    imageId = request.args(0,cast=int)
    image = db(db.Image.id == imageId).select() or redirect(URL('index'))
    if(image[0].created_by != auth.user.id):
        session.flash = "You cannot Edit this Image"
        redirect(URL('myImages'))
    else:
        if request.vars.imageFormTitle:
            imageBlob = request.vars.imageFormBlob
            imageTitle = request.vars.imageFormTitle
            imageId = request.vars.imageFormId
            if request.vars.imageFormPermission == "True":
                imagePermission = True
            else:
                imagePermission= False
            db(db.Image.id == imageId).update(ImageTitle = imageTitle,ImageCanvas=imageBlob,ImageViewPermission = imagePermission)
            redirect(URL('myImages'))
    return dict(image=image)

@auth.requires_login()
def updateImage():
    pass
    

def viewNotlogin():
    imageId = request.args(0,cast=int)
    if auth.user:
        redirect(URL('viewLogin',args=(imageId,),vars = dict(imageId=str(imageId))))
    image = db.Image(imageId) or redirect(URL('index'))
    db.comments.Image_Id.default = image.id
    imageComments = db(db.comments.Image_Id==image.id).select()
    return dict(image=image, imageComments=imageComments)

@auth.requires_login()
def viewLogin():
    imageId = request.args(0,cast=int)
    image = db.Image(imageId) or redirect(URL('index'))
    db.comments.Image_Id.default = image.id
    form = SQLFORM(db.comments,labels={'body':'Comment:'}).process() if auth.user else None
    imageComments = db(db.comments.Image_Id==image.id).select()
    return dict(image=image, imageComments=imageComments, form=form)

@auth.requires_login()
def deleteImage():
    imageId = request.args(0,cast=int)
    rowToDelete = db(db.Image.id == imageId).select() or redirect(URL('index'))
    if(rowToDelete[0].created_by == auth.user.id):
            db(db.Image.id ==imageId ).delete()
            session.flash = "Image Deleted"
            redirect(URL('myImages'))
    else:
        session.flash = "You cannot delete this Image"
        redirect(URL('myImages'))    

def user():
    """
    exposes:
    http://..../[app]/default/user/login
    http://..../[app]/default/user/logout
    http://..../[app]/default/user/register
    http://..../[app]/default/user/profile
    http://..../[app]/default/user/retrieve_password
    http://..../[app]/default/user/change_password
    http://..../[app]/default/user/manage_users (requires membership in
    http://..../[app]/default/user/bulk_register
    use @auth.requires_login()
        @auth.requires_membership('group name')
        @auth.requires_permission('read','table name',record_id)
    to decorate functions that need access control
    """
    return dict(form=auth())


@cache.action()
def download():
    """
    allows downloading of uploaded files
    http://..../[app]/default/download/[filename]
    """
    return response.download(request, db)


def call():
    """
    exposes services. for example:
    http://..../[app]/default/call/jsonrpc
    decorate with @services.jsonrpc the functions to expose
    supports xml, json, xmlrpc, jsonrpc, amfrpc, rss, csv
    """
    return service()
