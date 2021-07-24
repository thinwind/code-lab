# -*- coding: utf-8 -*-
'''
@Time   :  2021-7月-23 10:41
@Author :  Shang Yehua
@Email  :  niceshang@outlook.com
@Desc   :  
        sqlalchemy demo
'''
from sqlalchemy import create_engine
from sqlalchemy import text
from sqlalchemy.orm import Session

engine = create_engine("sqlite+pysqlite:///:memory:", echo=True, future=True)

with engine.connect() as conn:
    result = conn.execute(text("select 'hello world'"))
    print(result.all())
    
with engine.connect() as conn:
    conn.execute(text("CREATE TABLE some_table (x int, y int)"))
    conn.execute(
        text("INSERT INTO some_table (x, y) VALUES (:x, :y)"),
        [{"x":1,"y":1},{"x":2,"y":4}]
    )
    conn.commit()

#begin once
with engine.begin() as conn:
    conn.execute(
        text("INSERT INTO some_table (x, y) VALUES (:x, :y)"),
        [{"x":6,"y":8},{"x":9,"y":10}]
    )

print("==========>Attribute name ")

with engine.connect() as conn:
    result = conn.execute(text("select x,y from some_table"))
    for row in result:
        print(f"x: {row.x} y:{row.y}")
   
print("==========>Tuple assignment")
        
with engine.connect() as conn:
    result = conn.execute(text("select x,y from some_table"))
    for x,y in result:
        print(f"x: {x} y:{y}")
        
print("==========>Integer index")

with engine.connect() as conn:
    result = conn.execute(text("select x,y from some_table"))
    for row in result:
        print(f"x: {row[0]} y:{row[1]}")
        
print("==========>Mapping access")

with engine.connect() as conn:
    result = conn.execute(text("select x,y from some_table"))
    for dict_row in result.mappings():
        x = dict_row['x']
        y = dict_row['y']
        print(f"x: {x} y:{y}")
        
# multiple params
with engine.connect() as conn:
    conn.execute(
        text("INSERT INTO some_table (x, y) VALUES (:x, :y)"),
        [{"x": 11, "y": 12}, {"x": 13, "y": 14}]
    )
    conn.commit()
    
# single parameter
stmt = text("SELECT x, y FROM some_table WHERE y > :y ORDER BY x, y").bindparams(y=6)
with engine.connect() as conn:
    print("==========>Single parameter")
    result = conn.execute(stmt)
    for row in result:
        print(f"x: {row.x} y:{row.y}")
   
print("==========>Seession")
stmt = text("SELECT x, y FROM some_table WHERE y > :y ORDER BY x, y").bindparams(y=6)
with Session(engine) as session:
    result = session.execute(stmt)
    for row in result:
        print(f"x: {row.x} y:{row.y}")
        
print("==========>Seession update")
with Session(engine) as session:
    result = session.execute(
        text("UPDATE some_table SET y=:y WHERE x=:x"),
        [{"x": 9, "y":11}, {"x": 13, "y": 15}]
    )
    session.commit()
        
from sqlalchemy import MetaData
from sqlalchemy import Table, Column, Integer, String
from sqlalchemy import ForeignKey

# metadata=MetaData()

# user_table = Table(
#     "user_account",
#     metadata,
#     Column('id',Integer,primary_key=True),
#     Column('name',String(30)),
#     Column('fullname',String)
# )

# address_table = Table(
#     "address",
#     metadata,
#     Column('id',Integer,primary_key=True),
#     Column('user_id',ForeignKey('user_account.id'),nullable=False),
#     Column('email_address',String,nullable=False)
# )

# metadata.create_all(engine)

# from sqlalchemy.orm import registry
# mapper_registry = registry()

# Base = mapper_registry.generate_base()

from sqlalchemy.orm import declarative_base
from sqlalchemy.orm import relationship

Base = declarative_base()

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
        
print(repr(User.__table__))

Base.metadata.create_all(engine)

some_table = Table("some_table",Base.metadata,autoload_with=engine)


from sqlalchemy import insert

stmt = insert(User.__table__).values(name='spongebob',fullname='Spongebob Squarepants')
print(stmt)
compiled = stmt.compile()

print(repr(compiled.params))

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

stmt = select(user_table).where(user_table.c.name=='spongebob')
print(stmt)

with engine.connect() as conn:
    for row in conn.execute(stmt):
        print(row)

stmt = select(User).where(User.name=='sandy')

with Session(engine) as session:
    for row in session.execute(stmt):
        print(row)
        
print(select(user_table.c.name, user_table.c.fullname))
print(select(User))

stmt = select(User.name,User.fullname)

with Session(engine) as session:
    for row in session.execute(stmt):
        print(row)
        
stmt = select(User.name,Address).where(User.id == Address.user_id) \
        .order_by(Address.id)

with Session(engine) as session:
    for row in session.execute(stmt):
        print(row)