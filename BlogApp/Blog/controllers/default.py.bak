# -*- coding: utf-8 -*-
# this file is released under public domain and you can use without limitations

#########################################################################
## This is a sample controller
## - index is the default action of any application
## - user is required for authentication and authorization
## - download is for downloading files uploaded in the db (does streaming)
#########################################################################

def index():
    """
    example action using the internationalization operator T and flash
    rendered by views/default/index.html or views/generic.html

    if you need a simple wiki simply replace the two lines below with:
    return auth.wiki()
    """
    blogList = db(db.blog).select(db.blog.id,db.blog.title,db.blog.created_by,db.blog.created_on,orderby=~db.blog.created_on | db.blog.title)
    return dict(blogList = blogList)

def readnotlogin():
    blog_id = request.args(0,cast=int)
    if auth.user:
        redirect(URL('readlogin',args=(blog_id,),vars = dict(blog_id=str(blog_id))))
    blog = db.blog(blog_id) or redirect(URL('index'))
    db.comments.page_id.default = blog.id
    pagecomments = db(db.comments.page_id==blog.id).select()
    return dict(blog=blog, pagecomments=pagecomments)

@auth.requires_login()
def readlogin():
    blog_id = request.args(0,cast=int)
    blog = db.blog(blog_id) or redirect(URL('index'))
    db.comments.page_id.default = blog.id
    form = SQLFORM(db.comments,labels={'body':'Comment:'}).process() if auth.user else None
    pagecomments = db(db.comments.page_id==blog.id).select()
    return dict(blog=blog, pagecomments=pagecomments, form=form)
@auth.requires_login()
def myblog():
    """
    The displays public page of the user
    """
    fname =db(db.blog.created_by==auth.user_id).select().first()
    blogList = db(db.blog.created_by == auth.user.id).select(db.blog.id,db.blog.title,db.blog.created_by,db.blog.created_on,orderby=~db.blog.created_on | db.blog.title)
    return dict(blogList=blogList,fname=fname)
@auth.requires_login()
def create():
    """
    This function is for creating a new blog page
    """
    form = SQLFORM(db.blog,labels={'title':'Blog Title:','body':'Blog:'}).process()
    if form.accepted:
        session.flash = "Blog Created"
        redirect(URL('myblog'))
    return dict(form=form)

@auth.requires_login()
def deletepage():
    blog_id = request.args(0,cast=int)
    rowToDelete = db(db.blog.id == blog_id).select() or redirect(URL('index'))
    if(rowToDelete[0].created_by == auth.user.id):
            db(db.blog.id ==blog_id ).delete()
            session.flash = "Blog Deleted"
            redirect(URL('myblog'))
    else:
        session.flash = "You cannot this blog"
        redirect(URL('myblog'))
    
    
    
def blogview():
    """
    To display the blog and add comments
    """
    pass

@auth.requires_login()
def editpage():
    """
    still deciding
    """
    blog_id = request.args(0,cast=int)
    rowToEdit = db(db.blog.id == blog_id).select() or redirect(URL('index'))
    if(rowToEdit[0].created_by == auth.user.id):
        blogEdit = db.blog(blog_id) or redirect(URL('index'))
        form = SQLFORM(db.blog,blogEdit,showid = False,labels={'title':'Blog Title:','body':'Blog:'}).process()
        if form.accepted:
            session.flash = "Blog Edited"
            redirect(URL('myblog'))
    else:
        session.flash = "You Cannot Edit This Blog"
        redirect(URL('myblog'))
    return dict(form=form)


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
