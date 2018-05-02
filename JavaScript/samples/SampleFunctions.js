function myProduct(p1, p2) {
    // console.log("Hello JS");
    return p1 * p2;              // The function returns the product of p1 and p2
}


function playSound() {
    myMusic = new sound("sounds/fire.wav");
    myMusic.play();
}

function playSound2() {
    // create web audio api context
    var audioCtx = new (window.AudioContext || window.webkitAudioContext)();

    // create Oscillator node
    var oscillator = audioCtx.createOscillator();

    oscillator.type = 'square';
    oscillator.frequency.setValueAtTime(3000, audioCtx.currentTime); // value in hertz
    oscillator.connect(audioCtx.destination);
    oscillator.start();
}

