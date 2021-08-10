# -*- coding: utf-8 -*-
'''
@Time   :  2021-7月-24 14:12
@Author :  Shang Yehua
@Email  :  niceshang@outlook.com
@Desc   :  
        sqlalchemy test2
'''

from operator import add
from sqlalchemy import create_engine
from sqlalchemy import text
from sqlalchemy.orm import Session
from sqlalchemy import MetaData
from sqlalchemy import Table, Column, Integer, String
from sqlalchemy import ForeignKey
from sqlalchemy.orm import declarative_base
from sqlalchemy.orm import relationship
from sqlalchemy import insert
from sqlalchemy.sql.expression import join

Base = declarative_base()

engine = create_engine("sqlite+pysqlite:///:memory:", echo=True, future=True)


class User(Base):
    __tablename__='user_account'
    
    id = Column(Integer,primary_key=True)
    name = Column(String(30))
    fullname = Column(String)
    
    addresses = relationship("Address",back_populates="user")
    
    def __repr__(self) -> str:
        return f"User(id={self.id!r}, name={self.name!r}, fullname={self.fullname!r})"

class Address(Base):
    __tablename__ = 'address'
    
    id = Column(Integer,primary_key=True)
    email_address = Column(String, nullable=False)
    user_id = Column(Integer,ForeignKey('user_account.id'))
    
    user = relationship("User",back_populates="addresses")
    
    def __repr__(self) -> str:
        return f"Address(id={self.id!r}, email_address={self.email_address!r})"
        
Base.metadata.create_all(engine)

stmt = insert(User.__table__).values(name='spongebob',fullname='Spongebob Squarepants')

with engine.connect() as conn:
    result = conn.execute(stmt)
    conn.commit()
    print(result.inserted_primary_key)
    
with engine.connect() as conn:
    result = conn.execute(
        insert(User.__table__),
        [
            {"name":"sandy","fullname":"Sand Cheeks"},
            {"name":"patrick","fullname":"Patrick Star"},
        ]
    )
    conn.commit()
    

from sqlalchemy import select,bindparam

user_table = User.__table__
address_table = Address.__table__

scalar_subquery = (
    select(user_table.c.id).where(
        user_table.c.name==bindparam('username')
    ).scalar_subquery()
)

with engine.connect() as conn:
    result = conn.execute(
        insert(address_table).values(user_id=scalar_subquery),
        [
            {"username": 'spongebob', "email_address": "spongebob@sqlalchemy.org"},
            {"username": 'sandy', "email_address": "sandy@sqlalchemy.org"},
            {"username": 'sandy', "email_address": "sandy@squirrelpower.org"},
        ]
    )
    conn.commit()
    
from sqlalchemy import func,cast

stmt = select(("Username: "+user_table.c.name).label("username"),) \
        .order_by(user_table.c.name)
        
with engine.connect() as conn:
    for row in conn.execute(stmt):
        print(f"{row.username}")
        
stmt = select(
    text("'some phrase'"),user_table.c.name
).order_by(user_table.c.name)

with engine.connect() as conn:
    print(conn.execute(stmt).all())
    
print(user_table.c.name == 'squidward')
print(select(user_table).where(user_table.c.name == 'squidward'))
print(select(address_table.c.email_address) \
    .where(user_table.c.name=='squidward') \
    .where(address_table.c.user_id == user_table.c.id)
)

print(select(address_table.c.email_address) \
    .where(user_table.c.name=='squidward',
    address_table.c.user_id == user_table.c.id
    )
)

from sqlalchemy import and_, or_

print(
    select(Address.email_address)\
    .where(
        and_(
            or_(User.name=='squidard',User.name=='sandy'),
            Address.user_id == User.id
        )
    )
)

print(
    select(User).filter_by(name='spongebob',fullname='Spongebob Squarepants')
)

print(select(user_table.c.name))
print(select(user_table.c.name,address_table.c.email_address))

print(
    select(user_table.c.name,address_table.c.email_address) \
    .join_from(user_table,address_table)
)

print(
    select(user_table.c.name,address_table.c.email_address) \
    .join(address_table)
)

print(
    select(address_table.c.email_address) \
        .select_from(user_table).join(address_table)
)

print(
    select(func.count('*')).select_from(user_table)
)

print(
    select(address_table.c.email_address)\
        .select_from(user_table)\
        .join(address_table,user_table.c.id==address_table.c.user_id)
)

print(
    select(user_table).join(address_table,isouter=True)
)

print(
    select(user_table).join(address_table,full=True)
)

print(
    select(user_table).order_by(user_table.c.name)
)

print(
    select(User).order_by(User.fullname.desc())
)

count_fn = func.count(user_table.c.id)
print(count_fn)