package com.nice.tracker

import android.util.Log

class KotlinSamples {

    class Car(brand : String, age : Int) {
        var owner : String = brand

    }

    fun onStart() {

        val Val1 : Int = 5
        val Val2 : Int = 2


        fun Fun1 (a : Int, b: Int) {
            var c = a + b
            var d = 0

            when {
                c is Int -> d = 500
                c > 0 -> c = -200
            }

            var usingWhen = when(d) {
                0, 1, 56 -> 333
                else -> 555
            }



            Log.d("tag", "hi everybody, c = $c and d = $d and usingWhen = $usingWhen")

            for (x in 9 downTo  0 step 3) {
                Log.d("tag", "x = $x")
            }

            val items = setOf(1, 7, 4)


        }

        Fun1(Val1, Val2)


        data class Customer(val name: String, val age: Int) // создание POJO-класса со всеми геттерами и сеттерами


        //Массивы
/*        class Array<T> private constructor() {
            val size: Int
            fun get(index: Int): T
            fun set(index: Int, value: T): Unit

            fun iterator(): Iterator<T>
            // ...
        }*/

        var array1 = arrayOf(1, 17, 8, -5, 4)
        for (i in array1.indices)
            Log.d("tag", array1[i].toString())

        var array2 = Array(3, {i -> i * 2})
        //for (i in array2) Log.d("tag", "element $i = ${array2[i]}")
        var str = """my string"""
        Log.d("tag", "string = $str")
        for (c in str) {
            Log.d("tag", c.toString())
        }

        var usingIf = if (Val1 > Val2) {
            Val1
        } else {
            Val2
        }
        Log.d("tag", usingIf.toString())

        var car = Car("Audi", 12)
        var usingObject = car.owner
        Log.d("tag", usingObject)


        //Именованные аргументы функции
        //Аргументы могут задаваться как порядком, так и именем:

        fun reformat (text : String,
                      normalizeCase : Boolean = true,
                      upperCaseFirstLetter: Boolean = true,
                      divideByCamelHumps: Boolean = false,
                      wordSeparator: Char = ' ') {
            // some body

        }

        //вызов с использованием позиционных аргументов
        reformat("sometext", false, false, true, '-')

        //вызов с использованием именованных аргументов
        reformat(text = "sometext", upperCaseFirstLetter = false)


        //Нефиксированное число аргументов - обозначается ключевым словом varargs
        fun <T> asList(vararg ts: T): List<T> {
            val result = ArrayList<T>()
            for (t in ts) // ts - это массив (Array)
                result.add(t)
            return result
        }

        val list1 = asList(1, 2, 3)

        //При вызове vararg функции мы можем передать аргументы один-за-одним, например asList(1, 2, 3), или,
        // если у нас уже есть необходимый массив элементов и мы хотим передать его содержимое в нашу функцию,
        // использовать оператор spread (необходимо пометить массив знаком *):

        val a = arrayOf(1, 2, 3)
        val list2 = asList(-1, 0, *a, 4)

        //Функции-обобщения (Generic Functions)
        //Функции могут иметь обобщённые параметры, которые задаются треугольными скобками и помещаются перед именем функции
        fun <T> singletonList(item: T): List<T> {
            // ...
            var someList = listOf<T>()
            return someList
        }


    }

}