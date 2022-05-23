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
let vtable_IntNil = [IntNil_length, IntNil_sum, IntList_main];
let vtable_IntCons = [IntCons_length, IntCons_sum, IntList_main];
let vtable_IntList = [IntList_length, IntList_sum, IntList_main];
function IntList_constructor(self) {
_constructor(self);
}
function IntList_length(self) {
return 42;
}
function IntList_sum(self) {
return 42;
}
function IntList_main(self) {
}
function IntNil_constructor(self) {
IntList_constructor(self);
}
function IntNil_length(self) {
return 0;
}
function IntNil_sum(self) {
return 0;
}
function IntCons_constructor(self, headParam) {
IntList_constructor(self);
}
function IntCons_length(self) {
return 1;
}
function IntCons_sum(self) {
return self.head;
}
IntList_main()