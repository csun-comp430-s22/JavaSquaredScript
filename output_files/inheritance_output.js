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
let vtable_GoldenRetriever = [Animal_main, GoldenRetriever_bark];
let vtable_Animal = [Animal_main];
let vtable_Cat = [Animal_main, Cat_meows];
let vtable_Dog = [Animal_main, Dog_bark];
function Animal_constructor(self) {
_constructor(self);
}
function Animal_main(self) {
}
function Dog_constructor(self) {
Animal_constructor(self);
	let dog = makeObject(vtable_Dog, Dog_constructor);
}
function Dog_bark(self) {
	console.log("roof");
}
function Cat_constructor(self) {
Animal_constructor(self);
}
function Cat_meows(self) {
	console.log("meow");
}
function GoldenRetriever_constructor(self) {
Dog_constructor(self);
}
function GoldenRetriever_bark(self, y) {
	let dog = makeObject(vtable_GoldenRetriever, GoldenRetriever_constructor);
	let sound = doCall(dog, 1);
}
Animal_main()