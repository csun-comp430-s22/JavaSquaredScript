# Java²script
## Language Description: 
Object-oriented Java-like programming language 
now with operator overloading, classes and subclasses. 
The reasoning behind Javascript being our target language 
was due to it being a high-level language, making the project 
not impossible considering our group’s familiarity with both languages, 
while still ensuring the project would be non-trivial due to the 
differences between Object-oriented and scripting languages.

Members
---
| Name            |Email|
|-----------------|-----|
| Jesus Banuelos  |jesus.banuelos.951@my.csun.edu|
| Justin Kingston |justin.kingston.414@my.csun.edu|
| Jim Inong       |jim.inong.415@my.csun.edu|

## Features:
* Class-Based Inheritance Without Using JavaScript's Classes
* Subtyping
* Access Modifiers

## Abstract Syntax:
```
var is a variable
classname is the name of a class
methodname is the name of a method
object is a var, classname, String, this
strg is a string
intg is an integer
bool is a boolean
type ::= Int | Boolean | classname | String
primary_exp ::= var | strg | intg | `(` exp `)` | bool | exp`.`methodname(exp*)  |
new classname(exp*) | this
period_op ::= `.`
period_exp ::= primary_exp(period_op primary_exp)*
multiplicative_op ::= `*` | `/`
multiplicative_exp ::= period_exp( multiplicative_op period_exp)*
additive_op ::=  `+` | `-`
additive_exp ::= multiplicative_exp( additive_op multiplicative_exp)*
rel_op ::= `<` | `>`
rel_exp ::= additive_exp (rel_op additive_exp) //has to be 0 or 1 call
boolean_op ::=  `==` | `!=`
boolean_exp ::= rel_exp (boolean_op rel_exp) // has to be 0 or 1 call
exp ::= boolean_exp
varDec ::= type var
stmnt ::= varDec `;` |
while  (exp) stmnt |
break`;` |
{ stmnt* } |
if (exp) stmnt else stmnt |
return exp `;` |
print(exp) `;` | varDec `=`exp`;`

accessMod :: = public | private | protected
methodDef ::= accessMod type methodname(varDec*) stmnt
instanceDec ::= accessMod varDec`;`
classDef ::= class classname extends classname {
instanceDec*
constructor(varDec*) stmnt
methodDef*
}
programName ::= classDef* stmt //stmt is entry point
A  method named main is the entry point
```






