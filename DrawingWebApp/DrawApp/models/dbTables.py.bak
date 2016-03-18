# -*- coding: utf-8 -*-
db.define_table('Image', 
                Field('ImageTitle'),
                Field('ImageCanvas', 'blob'),
                Field('ImageUpload','upload',uploadfield='ImageCanvas'),
                Field('ImageViewPermission','boolean', default = False),
                Field('created_on', 'datetime', default=request.now),
                Field('created_by', 'reference auth_user',default=auth.user_id),
                format='%(ImageTitle)s %(created_by)s'
               ) 

db.Image.ImageTitle.requires = IS_NOT_EMPTY()  
db.Image.created_by.readable = db.Image.created_by.writable = False
db.Image.created_on.readable = db.Image.created_on.writable = False

db.define_table('comments',
                Field('Image_Id', 'reference Image'),
                Field('body', 'text'),
                Field('created_on', 'datetime', default=request.now),
                Field('created_by', 'reference auth_user',default=auth.user_id))

db.comments.body.requires = IS_NOT_EMPTY() 
db.comments.Image_Id.readable = db.comments.Image_Id.writable = False
db.comments.created_by.readable = db.comments.created_by.writable = False
db.comments.created_on.readable = db.comments.created_on.writable = False
