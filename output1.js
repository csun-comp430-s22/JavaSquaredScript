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
let vtable_BaseClass = [BaseClass_main, BaseClass_test];
function BaseClass_constructor(self) {
_constructor(self);
}
function BaseClass_main(self, x) {
	while (true) {
		console.log(5);
	}
}
function BaseClass_test(self) {
	console.log((5 + 6));
	let base = makeObject(vtable_BaseClass, BaseClass_constructor);
	if (true) {
		console.log("hello");
	} else {
		let p = 7;
	}
return false;
}
BaseClass_main()