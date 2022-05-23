function makeObject(vtable, constructor, ...params) {
let self = {};
  self.vtable = vtable;
  params.unshift(self);
  constructor.apply(this, params);
  return self;
}

function doCall(self, index, ...params) {
  params.unshift(self);
  return self.vtable[index].apply(this, params);
}

function Object_constructor(self) {}

let vtable_ = [];
let vtable_ExtendedClass = [BaseClass_main, BaseClass_test, BaseClass_test2, ExtendedClass_testing];
let vtable_BaseClass = [BaseClass_main, BaseClass_test, BaseClass_test2];
function BaseClass_constructor(self, y) {
_constructor(self);
}
function BaseClass_main(self, x) {
	while (true) {
		console.log(5);
	}
}
function BaseClass_test(self) {
	console.log((5 + 6));
	if (true) {
		console.log("hello");
	} else {
		let p = 7;
	}
return false;
}
function BaseClass_test2(self) {
	console.log(5);
}
function ExtendedClass_constructor(self) {
BaseClass_constructor(self);
}
function ExtendedClass_testing(self) {
	let base = makeObject(vtable_ExtendedClass, ExtendedClass_constructor);
	let base2 = makeObject(vtable_BaseClass, BaseClass_constructor, 2);
	let testVar = doCall(base, 2);
	let testingVar = doCall(base2, 2);
}
BaseClass_main()