package parking
var spots = emptyArray<Spot>()

fun main() {
    while (true) {
        val line = readln()
        if (line != "exit") {
            CommandHandler.handleCommand(line)
        } else break
    }


}

class CommandHandler() {
    companion object {
        fun handleCommand(line: String) {
            val command = line.split(" ")[0]
            when(command) {
                "park" -> park(line)
                "leave" -> leave(line)
                "create" -> create(line)
                "status" -> status()
                "reg_by_color" -> reg(line)
                "spot_by_color" -> spot(line)
                "spot_by_reg" -> spot(line)
            }

        }

        private fun status() {
            if (spots.isEmpty()) {
                println("Sorry, a parking lot has not been created.")
            } else if (spots.hasNoCars()) {
                println("Parking lot is empty.")
            } else {
                for (i in spots.indices) {
                    if (!spots[i].vacant) {
                        println("${i + 1} ${spots[i].car?.number} ${spots[i].car?.color}")
                    }

                }
            }
        }

        private fun reg(line: String) {
            if (spots.isEmpty()) {
                println("Sorry, a parking lot has not been created.")
            } else if (spots.hasNoCars()) {
                println("Parking lot is empty.")
            } else {
                val color = line.split(" ")[1]
                val carsList = mutableListOf<Car>()

                for (i in spots.indices) {
                    if (spots[i].car?.color?.lowercase() == color.lowercase()) {
                        carsList.add(spots[i].car!!)
                    }
                }
                if (carsList.isEmpty()) {
                    println("No cars with color $color were found.")
                } else {
                    println(carsList.map { it.number }.joinToString(", "))
                }
            }
        }

        private fun spot(line: String) {
            if (spots.isEmpty()) {
                println("Sorry, a parking lot has not been created.")
            } else if (spots.hasNoCars()) {
                println("Parking lot is empty.")
            } else {
                when(line.split(" ")[0]) {
                    "spot_by_color" -> {
                        val color = line.split(" ")[1]
                        val spotList = mutableListOf<Int>()

                        for (i in spots.indices) {
                            if (spots[i].car?.color?.lowercase() == color.lowercase()) {
                                spotList.add(i + 1)
                            }
                        }
                        if (spotList.isEmpty()) {
                            println("No cars with color $color were found.")
                        } else {
                            println(spotList.joinToString(", "))
                        }
                    }
                    "spot_by_reg" -> {
                        val reg = line.split(" ")[1]
                        val spotList = mutableListOf<Int>()

                        for (i in spots.indices) {
                            if (spots[i].car?.number?.lowercase()?.contains(reg.lowercase()) == true) {
                                spotList.add(i + 1)
                            }
                        }
                        if (spotList.isEmpty()) {
                            println("No cars with registration number $reg were found.")
                        } else {
                            println(spotList.joinToString(", "))
                        }
                    }
                }
            }
        }

        private fun create(line: String) {
            val spotsNumber = line.split(" ")[1].toInt()
            spots = Array(spotsNumber) { Spot(true) }
            println("Created a parking lot with $spotsNumber spots.")
        }

        private fun park(line: String) {
            if (spots.isEmpty()) {
                println("Sorry, a parking lot has not been created.")
                return
            }


            val number = line.split(" ")[1]
            val color = line.split(" ")[2]

            for (i in spots.indices) {
                if (spots[i].vacant) {
                    spots[i] = Spot(false, Car(number, color))
                    println("$color car parked in spot ${i + 1}.")
                    break
                }
                if (i == spots.lastIndex) {
                    println("Sorry, the parking lot is full.")
                }
            }
        }

        private fun leave(line: String) {
            if (spots.hasNoCars()) {
                println("Sorry, a parking lot has not been created.")
                return
            }

            val spotNumber = line.split(" ")[1].toInt()
            if (spots[spotNumber - 1].vacant) {
                println("There is no car in spot $spotNumber.")
            } else {
                spots[spotNumber - 1].vacant = true
                println("Spot $spotNumber is free.")
            }
        }
    }
}


class Spot {
    var car: Car? = null
    var vacant: Boolean = true
        set(value) {
            if (value) {
                car = null
            }
            field = value
        }

    constructor(vacant: Boolean, car: Car) {
        this.vacant = vacant
        if (vacant) {
            this.car = null
        } else {
            this.car = car
        }
    }

    constructor(vacant: Boolean) {
        this.vacant = vacant
    }

    override fun toString(): String {
        return "Spot(car=$car, vacant=$vacant)"
    }
}

fun Array<Spot>.hasNoCars(): Boolean {
    var isEmpty = true
    for (spot in spots) {
        if (!spot.vacant) {
            isEmpty = false
            break
        }
    }
    return isEmpty
}

class Car(val number: String, val color: String) {
    override fun toString(): String {
        return "Car(number='$number', color='$color')"
    }
}
