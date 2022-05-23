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
let vtable_A = [A_main, A_test];
function A_constructor(self, x, y) {
_constructor(self);
}
function A_main(self) {
}
function A_test(self, p) {
	let a = makeObject(vtable_A, A_constructor, 5, true);
	let x = doCall(a, 1, "hello");
	break();
}
A_main()