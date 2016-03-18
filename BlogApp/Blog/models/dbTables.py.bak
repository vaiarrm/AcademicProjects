# -*- coding: utf-8 -*-
db.define_table('blog', 
                Field('title'),
                Field('body', 'text'),
                Field('created_on', 'datetime', default=request.now),
                Field('created_by', 'reference auth_user',default=auth.user_id),
                format='%(title)s %(created_by)s')

db.define_table('comments',
                Field('page_id', 'reference blog'),
                Field('body', 'text'),
                Field('created_on', 'datetime', default=request.now),
                Field('created_by', 'reference auth_user',default=auth.user_id))

#db.blog.title.requires = IS_NOT_IN_DB(db, 'blog.title') 
db.blog.title.requires = IS_NOT_EMPTY()  
db.blog.body.requires = IS_NOT_EMPTY() 
db.blog.created_by.readable = db.blog.created_by.writable = False
db.blog.created_on.readable = db.blog.created_on.writable = False

db.comments.body.requires = IS_NOT_EMPTY() 
db.comments.page_id.readable = db.comments.page_id.writable = False
db.comments.created_by.readable = db.comments.created_by.writable = False
db.comments.created_on.readable = db.comments.created_on.writable = False
