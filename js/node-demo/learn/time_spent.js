const doSomething = () => console.log('test');
const measureDoingSomething = () => {
    console.time('doSomething');
    for (let index = 0; index < 100; index++) {
        doSomething();
    }
    console.timeEnd('doSomething');
}

measureDoingSomething();