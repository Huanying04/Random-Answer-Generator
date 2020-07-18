package net.nekomura.newrandomanswer

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.content_main.*

/**
 * @author 貓村幻影
 * @version 1.0.6.1
 */
class MainActivity : AppCompatActivity() {
    private var qNum : Int = 0
    private var answers4 = arrayOf("A", "B", "C", "D")
    private var answers5 = arrayOf("A", "B", "C", "D", "E")
    private var truefalse = arrayOf("O", "X")
    private val upperAlphaRegex = Regex("[A-Z]+")
    private val summonTypes = arrayOf(
        "單選題(A-D)",
        "單選題(A-E)",
        "單選題(第一選項, 最後選項)",
        "多選題(A-D)",
        "多選題(A-E)",
        "多選題(第一選項, 最後選項)",
        "多選項填空(第一選項, 最後選項)",
        "多選項填空(全部選項)",
        "是非題(O, X)",
        "隨機數字(最大值)",
        "隨機數字(最大值, 最小值)")
    private var rand: Int = 0
    private var summonHistory: StringBuffer = StringBuffer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar2: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar2)

        //main_summonType!!.setOnItemSelectedListener(this)
        val spinnerList = ArrayAdapter(this, android.R.layout.simple_spinner_item, summonTypes)
        spinnerList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        main_summonType.adapter = spinnerList

        main_summonType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent:AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (main_summonType.selectedItem.toString()) {
                    "單選題(第一選項, 最後選項)" -> { //單選題(第一選項, 最後選項)
                        InvisibleAll()
                        RandAnsFirst.visibility = View.VISIBLE
                        RandAnsLast.visibility = View.VISIBLE
                    }
                    "多選題(第一選項, 最後選項)" -> { //多選題(第一選項, 最後選項)
                        InvisibleAll()
                        RandAnsFirst.visibility = View.VISIBLE
                        RandAnsLast.visibility = View.VISIBLE
                    }
                    "多選項填空(第一選項, 最後選項)" -> { //多選項填空(第一選項, 最後選項)
                        InvisibleAll()
                        RandAnsFirst.visibility = View.VISIBLE
                        RandAnsLast.visibility = View.VISIBLE
                    }
                    "多選項填空(全部選項)" -> { //多選項填空(全部選項)
                        InvisibleAll()
                        RandAnsAll.visibility = View.VISIBLE
                        RandAnsAllHint.visibility = View.VISIBLE
                    }
                    "隨機數字(最大值)" -> { //隨機數字(最大值)
                        InvisibleAll()
                        RandIntMax.visibility = View.VISIBLE
                    }
                    "隨機數字(最大值, 最小值)" -> { //隨機數字(最大值, 最小值)
                        InvisibleAll()
                        RandIntMax.visibility = View.VISIBLE
                        RandIntMin.visibility = View.VISIBLE
                    }
                    else -> {
                        InvisibleAll()
                    }
                }
            }
        }

        //當按下"生成!"按鈕時
        summonButton.setOnClickListener {
            when (main_summonType.selectedItem.toString()) {
                "單選題(A-D)" -> {  //單選題(A-D)
                    qNum++
                    rand = (answers4.indices).random()
                    answerLabel.textSize = 270.toFloat()
                    answerLabel.text = answers4[rand]
                    QNumNumBox.text = qNum.toString()
                    summonHistory.append(answers4[rand])
                    if(summonHistory.split("\n")[summonHistory.split("\n").size - 1].length % 5 == 0)
                        summonHistory.append("\n")
                }
                "單選題(A-E)" -> {  //單選題(A-E)
                    qNum++
                    rand = (0 until answers5.size).random()
                    answerLabel.textSize = 270.toFloat()
                    answerLabel.text = answers5[rand]
                    QNumNumBox.text = qNum.toString()
                    summonHistory.append(answers5[rand])
                    if(summonHistory.split("\n")[summonHistory.split("\n").size - 1].length % 5 == 0)
                        summonHistory.append("\n")
                }
                "單選題(第一選項, 最後選項)" -> {  //單選題(第一選項, 最後選項)
                    try {
                        val randAnsFirst :Char = RandAnsFirst.getText().toString().get(0)
                        val randAnsLast :Char = RandAnsLast.getText().toString().get(0)
                        val allAnsArray = ArrayList<Char>()
                        var displayAns: String = ""

                        if (randAnsFirst.toString().matches(upperAlphaRegex) && randAnsLast.toString().matches(upperAlphaRegex)) {
                            if (randAnsFirst <= randAnsLast) {
                                for (ch: Char in randAnsFirst..randAnsLast) {
                                    allAnsArray.add(ch)
                                }

                                rand = (0 until allAnsArray.size).random()

                                answerLabel.textSize = 270.toFloat()
                                answerLabel.text = allAnsArray.get(rand).toString()
                                QNumNumBox.text = qNum.toString()
                                qNum++
                                summonHistory.append(allAnsArray.get(rand).toString())
                                if(summonHistory.split("\n")[summonHistory.split("\n").size - 1].length % 5 == 0)
                                    summonHistory.append("\n")
                            }else {
                                var builder = AlertDialog.Builder(this)
                                builder.setTitle("錯誤!")
                                builder.setMessage("第一個英文字母必須比最後一個小")
                                builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                                builder.show()
                            }
                        }else {
                            var builder = AlertDialog.Builder(this)
                            builder.setTitle("錯誤!")
                            builder.setMessage("只能輸入大寫英文字母")
                            builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                            builder.show()
                        }
                    }catch (e: StringIndexOutOfBoundsException) {
                        var builder = AlertDialog.Builder(this)
                        builder.setTitle("錯誤!")
                        builder.setMessage("輸入值不得為空")
                        builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                        builder.show()
                    }catch (e: Throwable) {
                        var builder = AlertDialog.Builder(this)
                        builder.setTitle("錯誤!")
                        builder.setMessage("出現未知錯誤，錯誤代碼: \n$e")
                        builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                        builder.show()
                    }
                }
                "多選題(A-D)" -> {  //多選題(A-D)
                    qNum++

                    var fiveAnsArray = ArrayList<Char>()
                    fiveAnsArray.add('A')
                    fiveAnsArray.add('B')
                    fiveAnsArray.add('C')
                    fiveAnsArray.add('D')
                    var firstAnsStr: String = ""
                    var lastAnsStr: String = ""
                    var howManyAnsRate: Int = (1..100).random()
                    var howManyAns: Int = 1
                    if (howManyAnsRate in 1..8) {
                        howManyAns = 1
                    }else if (howManyAnsRate in 9..50) {
                        howManyAns = 2
                    }else if (howManyAnsRate in 51..92) {
                        howManyAns = 3
                    }else if (howManyAnsRate in 93..100) {
                        howManyAns = 4
                    }else {
                        howManyAns = 4
                    }

                    for (i: Int in 0 until howManyAns) {
                        var rand: Int = (0 until fiveAnsArray.size).random()
                        firstAnsStr += fiveAnsArray[rand]
                        fiveAnsArray.remove(fiveAnsArray[rand])
                    }

                    for (i: Int in 0 until fiveAnsArray.size) {
                        fiveAnsArray.remove(fiveAnsArray[0])
                    }

                    fiveAnsArray.add('A')
                    fiveAnsArray.add('B')
                    fiveAnsArray.add('C')
                    fiveAnsArray.add('D')

                    for (i: Int in 0 until fiveAnsArray.size) {
                        if (firstAnsStr.indexOf(fiveAnsArray[i]) != -1) {
                            lastAnsStr += fiveAnsArray[i]
                        }
                    }

                    answerLabel.textSize = 90.toFloat()
                    answerLabel.setText(lastAnsStr)
                    QNumNumBox.setText(qNum.toString())
                    if(summonHistory.split("\n")[summonHistory.split("\n").size - 1].isNotEmpty())
                        summonHistory.append("\n")
                    summonHistory.append("[$lastAnsStr]")
                    summonHistory.append("\n")
                }
                "多選題(A-E)" -> { //多選題(A-E)
                    qNum++

                    var FiveAnsArray = ArrayList<Char>()
                    FiveAnsArray.add('A')
                    FiveAnsArray.add('B')
                    FiveAnsArray.add('C')
                    FiveAnsArray.add('D')
                    FiveAnsArray.add('E')
                    var FirstAnsStr: String = ""
                    var LastAnsStr: String = ""
                    var howManyAnsrate: Int = (1..100).random()
                    var howManyAns: Int = 1
                    if (howManyAnsrate in 1..5) {
                        howManyAns = 1
                    }else if (howManyAnsrate in 6..35) {
                        howManyAns = 2
                    }else if (howManyAnsrate in 36..65) {
                        howManyAns = 3
                    }else if (howManyAnsrate in 66..95) {
                        howManyAns = 4
                    }else if (howManyAnsrate in 96..100) {
                        howManyAns = 5
                    }else {
                        howManyAns = 5
                    }

                    for (i: Int in 0 until howManyAns) {
                        var rand: Int = (0 until FiveAnsArray.size).random()
                        FirstAnsStr += FiveAnsArray.get(rand)
                        FiveAnsArray.remove(FiveAnsArray.get(rand))
                    }

                    for (i: Int in 0 until FiveAnsArray.size) {
                        FiveAnsArray.remove(FiveAnsArray[0])
                    }

                    FiveAnsArray.add('A')
                    FiveAnsArray.add('B')
                    FiveAnsArray.add('C')
                    FiveAnsArray.add('D')
                    FiveAnsArray.add('E')

                    for (i: Int in 0 until FiveAnsArray.size) {
                        if (FirstAnsStr.indexOf(FiveAnsArray.get(i)) != -1) {
                            LastAnsStr += FiveAnsArray.get(i)
                        }
                    }

                    answerLabel.textSize = 90.toFloat()
                    answerLabel.text = LastAnsStr
                    QNumNumBox.text = qNum.toString()
                    if(summonHistory.split("\n")[summonHistory.split("\n").size - 1].isNotEmpty())
                        summonHistory.append("\n")
                    summonHistory.append("[$LastAnsStr]")
                    summonHistory.append("\n")
                }
                "多選題(第一選項, 最後選項)" -> {  //多選題(第一選項, 最後選項)
                    try {
                        var randAnsFirst :Char = RandAnsFirst.text.toString().get(0)
                        var randAnsLast :Char = RandAnsLast.text.toString().get(0)
                        var allAnsArray = ArrayList<Char>()
                        var howManyAns = (1..randAnsLast.toInt()-randAnsFirst.toInt()).random()
                        var displayAns: String = ""
                        var firstAns: String = ""
                        var lastAns: String = ""

                        if (randAnsFirst.toString().matches(upperAlphaRegex) && randAnsLast.toString().matches(upperAlphaRegex)) {
                            if (randAnsFirst <= randAnsLast) {
                                for (ch: Char in randAnsFirst..randAnsLast) {
                                    allAnsArray.add(ch)
                                }

                                rand = (0 until allAnsArray.size).random()

                                for (i: Int in 0 until howManyAns) {
                                    var rand: Int = (0 until allAnsArray.size).random()
                                    firstAns = firstAns + allAnsArray.get(rand)
                                    allAnsArray.removeAt(rand)
                                }

                                allAnsArray = ArrayList<Char>()

                                for (ch: Char in randAnsFirst..randAnsLast) {
                                    allAnsArray.add(ch)
                                }

                                for (i: Int in 0 until allAnsArray.size) {
                                    if (firstAns.indexOf(allAnsArray.get(i)) != -1) {
                                        lastAns += allAnsArray.get(i)
                                    }
                                }

                                answerLabel.textSize = 60.toFloat()
                                answerLabel.setText(lastAns)
                                QNumNumBox.setText(qNum.toString())
                                qNum++
                                if(summonHistory.split("\n")[summonHistory.split("\n").size - 1].isNotEmpty())
                                    summonHistory.append("\n")
                                summonHistory.append("[$lastAns]")
                            }else {
                                var builder = AlertDialog.Builder(this)
                                builder.setTitle("錯誤!")
                                builder.setMessage("第一個英文字母必須比最後一個小")
                                builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                                builder.show()
                            }
                        }else {
                            var builder = AlertDialog.Builder(this)
                            builder.setTitle("錯誤!")
                            builder.setMessage("只能輸入大寫英文字母")
                            builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                            builder.show()
                        }
                    }catch (e: StringIndexOutOfBoundsException) {
                        var builder = AlertDialog.Builder(this)
                        builder.setTitle("錯誤!")
                        builder.setMessage("輸入值不得為空")
                        builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                        builder.show()
                    }catch (e: Throwable) {
                        var builder = AlertDialog.Builder(this)
                        builder.setTitle("錯誤!")
                        builder.setMessage("出現未知錯誤，錯誤代碼: \n$e")
                        builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                        builder.show()
                    }
                }
                "多選項填空(第一選項, 最後選項)" -> {  //多選項填空(第一選項, 最後選項)
                    try {
                        qNum++
                        var randAnsFirst :Char = RandAnsFirst.getText().toString()[0]
                        var randAnsLast :Char = RandAnsLast.getText().toString()[0]
                        var allAnsArray = ArrayList<Char>()
                        var displayAns: String = ""

                        if (randAnsFirst.toString().matches(upperAlphaRegex) && randAnsLast.toString().matches(upperAlphaRegex)) {
                            if (randAnsFirst <= randAnsLast) {
                                for (ch: Char in randAnsFirst..randAnsLast) {
                                    allAnsArray.add(ch)
                                }

                                for (i: Int in 0 until allAnsArray.size) {
                                    rand = (0 until allAnsArray.size).random()
                                    displayAns += allAnsArray.get(rand)
                                    allAnsArray.removeAt(rand)
                                }
                                answerLabel.textSize = 30.toFloat()
                                answerLabel.setText(displayAns)
                                QNumNumBox.setText(qNum.toString())
                                if(summonHistory.split("\n")[summonHistory.split("\n").size - 1].isNotEmpty())
                                    summonHistory.append("\n")
                                summonHistory.append("[$displayAns]")
                                summonHistory.append("\n")
                            }else {
                                var builder = AlertDialog.Builder(this)
                                builder.setTitle("錯誤!")
                                builder.setMessage("第一個英文字母必須比最後一個小")
                                builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                                builder.show()
                            }
                        }else {
                            var builder = AlertDialog.Builder(this)
                            builder.setTitle("錯誤!")
                            builder.setMessage("只能輸入大寫英文字母")
                            builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                            builder.show()
                        }
                    }catch (e: StringIndexOutOfBoundsException) {
                        var builder = AlertDialog.Builder(this)
                        builder.setTitle("錯誤!")
                        builder.setMessage("輸入值不得為空")
                        builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                        builder.show()
                    }catch (e: Throwable) {
                        var builder = AlertDialog.Builder(this)
                        builder.setTitle("錯誤!")
                        builder.setMessage("出現未知錯誤，錯誤代碼: \n$e")
                        builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                        builder.show()
                    }
                }
                "多選項填空(全部選項)" -> {  //多選項填空(全部選項)
                    try {
                        qNum++
                        var allAnsArray = ArrayList<Char>()
                        var displayAns: String = ""

                        for (i: Int in RandAnsAll.getText().toString().indices) {
                            allAnsArray.add(RandAnsAll.getText().toString().get(i))
                        }
                        for (i: Int in 0 until allAnsArray.size) {
                            rand = (0 until allAnsArray.size).random()
                            displayAns += allAnsArray.get(rand)
                            allAnsArray.removeAt(rand)
                        }

                        if (!(displayAns.equals(""))) {
                            answerLabel.textSize = 30.toFloat()
                            answerLabel.setText(displayAns)
                            QNumNumBox.setText(qNum.toString())
                            if(summonHistory.split("\n")[summonHistory.split("\n").size - 1].isNotEmpty())
                                summonHistory.append("\n")
                            summonHistory.append("[$displayAns]")
                            summonHistory.append("\n")
                        }else {
                            var builder = AlertDialog.Builder(this)
                            builder.setTitle("錯誤!")
                            builder.setMessage("輸入值不得為空")
                            builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                            builder.show()
                        }
                    }catch (e: Throwable) {
                        var builder = AlertDialog.Builder(this)
                        builder.setTitle("錯誤!")
                        builder.setMessage("出現未知錯誤，錯誤代碼: \n$e")
                        builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                        builder.show()
                    }
                }
                "是非題(O, X)" -> {  //是非題
                    qNum++
                    rand = (0 until truefalse.size).random()
                    answerLabel.textSize = 270.toFloat()
                    answerLabel.text = truefalse[rand]
                    QNumNumBox.text = qNum.toString()
                    summonHistory.append(truefalse[rand])
                    if(summonHistory.split("\n")[summonHistory.split("\n").size - 1].length % 5 == 0)
                        summonHistory.append("\n")
                }
                "隨機數字(最大值)" -> {  //最大值隨機數字
                    try {
                        var randomIntMax :Int = RandIntMax.text.toString().toInt()

                        if (randomIntMax < 1) {
                            var builder = AlertDialog.Builder(this)
                            builder.setTitle("錯誤!")
                            builder.setMessage("輸入值不得小於1，若最大值慾設為0，請使用\"隨機數字(最大值, 最小值)\"")
                            builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                            builder.show()
                        }else if(randomIntMax.toLong() > 2147483647L) {
                            var builder = AlertDialog.Builder(this)
                            builder.setTitle("錯誤!")
                            builder.setMessage("輸入值不得大於2147483647")
                            builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                            builder.show()
                        }else {
                            qNum++
                            rand = (1..randomIntMax).random()
                            answerLabel.textSize = 60.toFloat()
                            answerLabel.setText(rand.toString())
                            QNumNumBox.setText(qNum.toString())
                            if(summonHistory.split("\n")[summonHistory.split("\n").size - 1].isNotEmpty())
                                summonHistory.append("\n")
                            summonHistory.append("($rand)")
                            summonHistory.append("\n")
                        }
                    }catch (e: NumberFormatException) {
                        if(RandIntMax.text.isEmpty()) {
                            var builder = AlertDialog.Builder(this)
                            builder.setTitle("錯誤!")
                            builder.setMessage("輸入值不得為空")
                            builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                            builder.show()
                        }else if(RandIntMax.text.toString().toLong() > 2147483647L) {
                            var builder = AlertDialog.Builder(this)
                            builder.setTitle("錯誤!")
                            builder.setMessage("輸入值不得大於2147483647")
                            builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                            builder.show()
                        }else {
                            var builder = AlertDialog.Builder(this)
                            builder.setTitle("錯誤!")
                            builder.setMessage("出現錯誤，錯誤代碼: \n$e")
                            builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                            builder.show()
                        }
                    }catch (e: Throwable) {
                        var builder = AlertDialog.Builder(this)
                        builder.setTitle("錯誤!")
                        builder.setMessage("出現未知錯誤，錯誤代碼: \n$e")
                        builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                        builder.show()
                    }
                }
                "隨機數字(最大值, 最小值)" -> {  //最小值-最大值隨機數字
                    try {
                        var randomIntMax :Int = RandIntMax.text.toString().toInt()
                        var randomIntMin :Int = RandIntMin.text.toString().toInt()
                        if (randomIntMin > randomIntMax) {
                            var builder = AlertDialog.Builder(this)
                            builder.setTitle("錯誤!")
                            builder.setMessage("最小值不得大於最大值")
                            builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                            builder.show()
                        }else {
                            qNum++
                            rand = (randomIntMin..randomIntMax).random()
                            answerLabel.textSize = 60.toFloat()
                            answerLabel.setText(rand.toString())
                            QNumNumBox.setText(qNum.toString())
                            if(summonHistory.split("\n")[summonHistory.split("\n").size - 1].isNotEmpty())
                                summonHistory.append("\n")
                            summonHistory.append("($rand)")
                            summonHistory.append("\n")
                        }
                    }catch (e: NumberFormatException) {
                        if(RandIntMax.text.isEmpty() || RandIntMin.text.isEmpty()) {
                            var builder = AlertDialog.Builder(this)
                            builder.setTitle("錯誤!")
                            builder.setMessage("輸入值不得為空")
                            builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                            builder.show()
                        }else if(RandIntMax.text.toString().toLong() > 2147483647L || RandIntMin.text.toString().toLong() > 2147483647L) {
                            var builder = AlertDialog.Builder(this)
                            builder.setTitle("錯誤!")
                            builder.setMessage("輸入值不得大於2147483647")
                            builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                            builder.show()
                        }else {
                            var builder = AlertDialog.Builder(this)
                            builder.setTitle("錯誤!")
                            builder.setMessage("出現錯誤，錯誤代碼: \n$e")
                            builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                            builder.show()
                        }
                    }catch (e: Throwable) {
                        var builder = AlertDialog.Builder(this)
                        builder.setTitle("錯誤!")
                        builder.setMessage("出現未知錯誤，錯誤代碼: \n$e")
                        builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                        builder.show()
                    }
                }
                else -> {
                    var builder = AlertDialog.Builder(this)
                    builder.setTitle("錯誤!")
                    builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                    builder.show()
                }
            }
        }

        summonHistoryButton.setOnClickListener {
            var builder = AlertDialog.Builder(this)
            builder.setTitle("生成歷史")
            builder.setMessage(summonHistory.toString())
            builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
            builder.setNeutralButton("複製到剪貼簿") { dialogInterface: DialogInterface, i: Int ->
                val clipboard: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText(summonHistory.toString(), summonHistory.toString())
                clipboard.setPrimaryClip(clip)
            }
            builder.show()
        }

        ResetButton.setOnClickListener{
            var builder = AlertDialog.Builder(this)
            builder.setTitle("警告")
            builder.setIconAttribute(android.R.attr.alertDialogIcon)
            builder.setMessage("這個動作會使\"生成歷史\"及\"題號\"通通歸零，將不會保存。是否繼續？")
            builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->
                qNum = 0
                QNumNumBox.setText(qNum.toString())
                summonHistory = StringBuffer()
                val toast = Toast.makeText(applicationContext, "已歸零", Toast.LENGTH_SHORT)
                toast.show()
            }
            builder.setNegativeButton("取消") { dialogInterface: DialogInterface, i: Int ->}
            builder.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        var inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val i: Int = item.itemId

        if (i == R.id.action_about) {
            var builder = AlertDialog.Builder(this)
            //userInput.setText(Html.fromHtml(getResources().getString(R.string.action_about)))
            builder.setTitle("關於")
            builder.setMessage(R.string.about_string)
            builder.setCancelable(true)
            //builder.setView(R.layout.activity_about)
            builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
            var finalAlert: AlertDialog = builder.create()
            finalAlert.show()
            (finalAlert.findViewById(android.R.id.message) as TextView).movementMethod =
                LinkMovementMethod.getInstance()
        }

        return super.onOptionsItemSelected(item)
    }

    fun InvisibleAll() {
        RandIntMax.visibility = View.INVISIBLE
        RandIntMin.visibility = View.INVISIBLE
        RandAnsFirst.visibility = View.INVISIBLE
        RandAnsLast.visibility = View.INVISIBLE
        RandAnsAll.visibility = View.INVISIBLE
        RandAnsAllHint.visibility = View.INVISIBLE
    }
}