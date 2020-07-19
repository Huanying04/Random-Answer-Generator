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
 * @version 1.0.6.3
 */
class MainActivity : AppCompatActivity() {
    private var qNum : Int = 0
    private var answers4 = arrayOf("A", "B", "C", "D")
    private var answers5 = arrayOf("A", "B", "C", "D", "E")
    private var truefalse = arrayOf("O", "X")
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
                        invisibleAll()
                        RandAnsFirst.visibility = View.VISIBLE
                        RandAnsLast.visibility = View.VISIBLE
                    }
                    "多選題(第一選項, 最後選項)" -> { //多選題(第一選項, 最後選項)
                        invisibleAll()
                        RandAnsFirst.visibility = View.VISIBLE
                        RandAnsLast.visibility = View.VISIBLE
                    }
                    "多選項填空(第一選項, 最後選項)" -> { //多選項填空(第一選項, 最後選項)
                        invisibleAll()
                        RandAnsFirst.visibility = View.VISIBLE
                        RandAnsLast.visibility = View.VISIBLE
                    }
                    "多選項填空(全部選項)" -> { //多選項填空(全部選項)
                        invisibleAll()
                        RandAnsAll.visibility = View.VISIBLE
                        RandAnsAllHint.visibility = View.VISIBLE
                    }
                    "隨機數字(最大值)" -> { //隨機數字(最大值)
                        invisibleAll()
                        RandIntMax.visibility = View.VISIBLE
                    }
                    "隨機數字(最大值, 最小值)" -> { //隨機數字(最大值, 最小值)
                        invisibleAll()
                        RandIntMax.visibility = View.VISIBLE
                        RandIntMin.visibility = View.VISIBLE
                    }
                    else -> {
                        invisibleAll()
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
                    rand = (answers5.indices).random()
                    answerLabel.textSize = 270.toFloat()
                    answerLabel.text = answers5[rand]
                    QNumNumBox.text = qNum.toString()
                    summonHistory.append(answers5[rand])
                    if(summonHistory.split("\n")[summonHistory.split("\n").size - 1].length % 5 == 0)
                        summonHistory.append("\n")
                }
                "單選題(第一選項, 最後選項)" -> {  //單選題(第一選項, 最後選項)
                    try {
                        val randAnsFirst :Char = RandAnsFirst.text.toString()[0]
                        val randAnsLast :Char = RandAnsLast.text.toString()[0]
                        val allAnsArray = ArrayList<Char>()

                        if (randAnsFirst.toString().matches(Regex("[A-Z]+")) && randAnsLast.toString().matches(Regex("[A-Z]+"))) {
                            if (randAnsFirst <= randAnsLast) {
                                for (ch: Char in randAnsFirst..randAnsLast) {
                                    allAnsArray.add(ch)
                                }

                                rand = (0 until allAnsArray.size).random()

                                answerLabel.textSize = 270.toFloat()
                                answerLabel.text = allAnsArray[rand].toString()
                                QNumNumBox.text = qNum.toString()
                                qNum++
                                summonHistory.append(allAnsArray[rand].toString())
                                if(summonHistory.split("\n")[summonHistory.split("\n").size - 1].length % 5 == 0)
                                    summonHistory.append("\n")
                            }else {
                                val builder = AlertDialog.Builder(this)
                                builder.setTitle("錯誤!")
                                builder.setMessage("第一個英文字母必須比最後一個小")
                                builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                                builder.show()
                            }
                        }else {
                            val builder = AlertDialog.Builder(this)
                            builder.setTitle("錯誤!")
                            builder.setMessage("只能輸入大寫英文字母")
                            builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                            builder.show()
                        }
                    }catch (e: StringIndexOutOfBoundsException) {
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("錯誤!")
                        builder.setMessage("輸入值不得為空")
                        builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                        builder.show()
                    }catch (e: Throwable) {
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("錯誤!")
                        builder.setMessage("出現未知錯誤，錯誤代碼: \n$e")
                        builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                        builder.show()
                    }
                }
                "多選題(A-D)" -> {  //多選題(A-D)
                    qNum++

                    val fiveAnsArray = ArrayList<Char>()
                    fiveAnsArray.add('A')
                    fiveAnsArray.add('B')
                    fiveAnsArray.add('C')
                    fiveAnsArray.add('D')
                    var firstAnsStr = ""
                    var lastAnsStr = ""
                    val howManyAnsRate: Int = (1..100).random()
                    val howManyAns: Int
                    howManyAns = when (howManyAnsRate) {
                        in 1..8 -> 1
                        in 9..50 -> 2
                        in 51..92 -> 3
                        in 93..100 -> 4
                        else -> 4
                    }

                    for (i: Int in 0 until howManyAns) {
                        val rand: Int = (0 until fiveAnsArray.size).random()
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
                    answerLabel.text = lastAnsStr
                    QNumNumBox.text = qNum.toString()
                    if(summonHistory.split("\n")[summonHistory.split("\n").size - 1].isNotEmpty())
                        summonHistory.append("\n")
                    summonHistory.append("[$lastAnsStr]")
                    summonHistory.append("\n")
                }
                "多選題(A-E)" -> { //多選題(A-E)
                    qNum++

                    val fiveAnsArray = ArrayList<Char>()
                    fiveAnsArray.add('A')
                    fiveAnsArray.add('B')
                    fiveAnsArray.add('C')
                    fiveAnsArray.add('D')
                    fiveAnsArray.add('E')
                    var firstAnsStr = ""
                    var lastAnsStr = ""
                    val howManyAnsRate: Int = (1..100).random()
                    val howManyAns: Int
                    howManyAns = when (howManyAnsRate) {
                        in 1..5 -> 1
                        in 6..35 -> 2
                        in 36..65 -> 3
                        in 66..95 -> 4
                        in 96..100 -> 5
                        else -> 5
                    }

                    for (i: Int in 0 until howManyAns) {
                        val rand: Int = (0 until fiveAnsArray.size).random()
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
                    fiveAnsArray.add('E')

                    for (i: Int in 0 until fiveAnsArray.size) {
                        if (firstAnsStr.indexOf(fiveAnsArray[i]) != -1) {
                            lastAnsStr += fiveAnsArray[i]
                        }
                    }

                    answerLabel.textSize = 90.toFloat()
                    answerLabel.text = lastAnsStr
                    QNumNumBox.text = qNum.toString()
                    if(summonHistory.split("\n")[summonHistory.split("\n").size - 1].isNotEmpty())
                        summonHistory.append("\n")
                    summonHistory.append("[$lastAnsStr]")
                    summonHistory.append("\n")
                }
                "多選題(第一選項, 最後選項)" -> {  //多選題(第一選項, 最後選項)
                    try {
                        val randAnsFirst :Char = RandAnsFirst.text.toString()[0]
                        val randAnsLast :Char = RandAnsLast.text.toString()[0]
                        var allAnsArray = ArrayList<Char>()
                        val howManyAns = (1..randAnsLast.toInt()-randAnsFirst.toInt()).random()
                        var firstAns = ""
                        var lastAns = ""

                        if (randAnsFirst.toString().matches(Regex("[A-Z]+")) && randAnsLast.toString().matches(Regex("[A-Z]+"))) {
                            if (randAnsFirst <= randAnsLast) {
                                for (ch: Char in randAnsFirst..randAnsLast) {
                                    allAnsArray.add(ch)
                                }

                                rand = (0 until allAnsArray.size).random()

                                for (i: Int in 0 until howManyAns) {
                                    val rand: Int = (0 until allAnsArray.size).random()
                                    firstAns += allAnsArray[rand]
                                    allAnsArray.removeAt(rand)
                                }

                                allAnsArray = ArrayList()

                                for (ch: Char in randAnsFirst..randAnsLast) {
                                    allAnsArray.add(ch)
                                }

                                for (i: Int in 0 until allAnsArray.size) {
                                    if (firstAns.indexOf(allAnsArray[i]) != -1) {
                                        lastAns += allAnsArray[i]
                                    }
                                }

                                answerLabel.textSize = 60.toFloat()
                                answerLabel.text = lastAns
                                QNumNumBox.text = qNum.toString()
                                qNum++
                                if(summonHistory.split("\n")[summonHistory.split("\n").size - 1].isNotEmpty())
                                    summonHistory.append("\n")
                                summonHistory.append("[$lastAns]")
                            }else {
                                val builder = AlertDialog.Builder(this)
                                builder.setTitle("錯誤!")
                                builder.setMessage("第一個英文字母必須比最後一個小")
                                builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                                builder.show()
                            }
                        }else {
                            val builder = AlertDialog.Builder(this)
                            builder.setTitle("錯誤!")
                            builder.setMessage("只能輸入大寫英文字母")
                            builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                            builder.show()
                        }
                    }catch (e: StringIndexOutOfBoundsException) {
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("錯誤!")
                        builder.setMessage("輸入值不得為空")
                        builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                        builder.show()
                    }catch (e: Throwable) {
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("錯誤!")
                        builder.setMessage("出現未知錯誤，錯誤代碼: \n$e")
                        builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                        builder.show()
                    }
                }
                "多選項填空(第一選項, 最後選項)" -> {  //多選項填空(第一選項, 最後選項)
                    try {
                        qNum++
                        val randAnsFirst :Char = RandAnsFirst.text.toString()[0]
                        val randAnsLast :Char = RandAnsLast.text.toString()[0]
                        val allAnsArray = ArrayList<Char>()
                        var displayAns = ""

                        if (randAnsFirst.toString().matches(Regex("[A-Z]+")) && randAnsLast.toString().matches(Regex("[A-Z]+"))) {
                            if (randAnsFirst <= randAnsLast) {
                                for (ch: Char in randAnsFirst..randAnsLast) {
                                    allAnsArray.add(ch)
                                }

                                for (i: Int in 0 until allAnsArray.size) {
                                    rand = (0 until allAnsArray.size).random()
                                    displayAns += allAnsArray[rand]
                                    allAnsArray.removeAt(rand)
                                }
                                answerLabel.textSize = 30.toFloat()
                                answerLabel.text = displayAns
                                QNumNumBox.text = qNum.toString()
                                if(summonHistory.split("\n")[summonHistory.split("\n").size - 1].isNotEmpty())
                                    summonHistory.append("\n")
                                summonHistory.append("[$displayAns]")
                                summonHistory.append("\n")
                            }else {
                                val builder = AlertDialog.Builder(this)
                                builder.setTitle("錯誤!")
                                builder.setMessage("第一個英文字母必須比最後一個小")
                                builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                                builder.show()
                            }
                        }else {
                            val builder = AlertDialog.Builder(this)
                            builder.setTitle("錯誤!")
                            builder.setMessage("只能輸入大寫英文字母")
                            builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                            builder.show()
                        }
                    }catch (e: StringIndexOutOfBoundsException) {
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("錯誤!")
                        builder.setMessage("輸入值不得為空")
                        builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                        builder.show()
                    }catch (e: Throwable) {
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("錯誤!")
                        builder.setMessage("出現未知錯誤，錯誤代碼: \n$e")
                        builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                        builder.show()
                    }
                }
                "多選項填空(全部選項)" -> {  //多選項填空(全部選項)
                    try {
                        qNum++
                        val allAnsArray = ArrayList<Char>()
                        var displayAns = ""

                        for (i: Int in RandAnsAll.text.toString().indices) {
                            allAnsArray.add(RandAnsAll.text.toString()[i])
                        }
                        for (i: Int in 0 until allAnsArray.size) {
                            rand = (0 until allAnsArray.size).random()
                            displayAns += allAnsArray[rand]
                            allAnsArray.removeAt(rand)
                        }

                        if (displayAns != "") {
                            answerLabel.textSize = 30.toFloat()
                            answerLabel.text = displayAns
                            QNumNumBox.text = qNum.toString()
                            if(summonHistory.split("\n")[summonHistory.split("\n").size - 1].isNotEmpty())
                                summonHistory.append("\n")
                            summonHistory.append("[$displayAns]")
                            summonHistory.append("\n")
                        }else {
                            val builder = AlertDialog.Builder(this)
                            builder.setTitle("錯誤!")
                            builder.setMessage("輸入值不得為空")
                            builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                            builder.show()
                        }
                    }catch (e: Throwable) {
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("錯誤!")
                        builder.setMessage("出現未知錯誤，錯誤代碼: \n$e")
                        builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                        builder.show()
                    }
                }
                "是非題(O, X)" -> {  //是非題
                    qNum++
                    rand = (truefalse.indices).random()
                    answerLabel.textSize = 270.toFloat()
                    answerLabel.text = truefalse[rand]
                    QNumNumBox.text = qNum.toString()
                    summonHistory.append(truefalse[rand])
                    if(summonHistory.split("\n")[summonHistory.split("\n").size - 1].length % 5 == 0)
                        summonHistory.append("\n")
                }
                "隨機數字(最大值)" -> {  //最大值隨機數字
                    try {

                        when (val randomNumMax = RandIntMax.text.toString().toLong()) {
                            in Long.MIN_VALUE..0L -> {
                                val builder = AlertDialog.Builder(this)
                                builder.setTitle("錯誤!")
                                builder.setMessage("輸入值不得小於1，若最大值慾設為0，請使用\"隨機數字(最大值, 最小值)\"")
                                builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                                builder.show()
                            }
                            in 2147483648L..Long.MAX_VALUE -> {
                                val builder = AlertDialog.Builder(this)
                                builder.setTitle("錯誤!")
                                builder.setMessage("輸入值不得大於2147483647")
                                builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                                builder.show()
                            }
                            else -> {
                                qNum++
                                rand = (1..randomNumMax.toInt()).random()
                                answerLabel.textSize = 60.toFloat()
                                answerLabel.text = rand.toString()
                                QNumNumBox.text = qNum.toString()
                                if(summonHistory.split("\n")[summonHistory.split("\n").size - 1].isNotEmpty())
                                    summonHistory.append("\n")
                                summonHistory.append("($rand)")
                                summonHistory.append("\n")
                            }
                        }
                    }catch (e: NumberFormatException) {

                        when (RandIntMax.text.toString()) {
                            "" -> {
                                val builder = AlertDialog.Builder(this)
                                builder.setTitle("錯誤!")
                                builder.setMessage("輸入值不得為空")
                                builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                                builder.show()
                            }
                            else -> {
                                val builder = AlertDialog.Builder(this)
                                builder.setTitle("錯誤!")
                                builder.setMessage("出現錯誤，錯誤代碼: \n$e")
                                builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                                builder.show()
                            }
                        }

                    }catch (e: Throwable) {
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("錯誤!")
                        builder.setMessage("出現未知錯誤，錯誤代碼: \n$e")
                        builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                        builder.show()
                    }
                }
                "隨機數字(最大值, 最小值)" -> {  //最小值-最大值隨機數字
                    try {
                        val randomIntMax :Int = RandIntMax.text.toString().toInt()
                        val randomIntMin :Int = RandIntMin.text.toString().toInt()
                        if (randomIntMin > randomIntMax) {
                            val builder = AlertDialog.Builder(this)
                            builder.setTitle("錯誤!")
                            builder.setMessage("最小值不得大於最大值")
                            builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                            builder.show()
                        }else {
                            qNum++
                            rand = (randomIntMin..randomIntMax).random()
                            answerLabel.textSize = 60.toFloat()
                            answerLabel.text = rand.toString()
                            QNumNumBox.text = qNum.toString()
                            if(summonHistory.split("\n")[summonHistory.split("\n").size - 1].isNotEmpty())
                                summonHistory.append("\n")
                            summonHistory.append("($rand)")
                            summonHistory.append("\n")
                        }
                    }catch (e: NumberFormatException) {
                        if(RandIntMax.text.isEmpty() || RandIntMin.text.isEmpty()) {
                            val builder = AlertDialog.Builder(this)
                            builder.setTitle("錯誤!")
                            builder.setMessage("輸入值不得為空")
                            builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                            builder.show()
                        }else if(RandIntMax.text.toString().toLong() > 2147483647L || RandIntMin.text.toString().toLong() > 2147483647L) {
                            val builder = AlertDialog.Builder(this)
                            builder.setTitle("錯誤!")
                            builder.setMessage("輸入值不得大於2147483647")
                            builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                            builder.show()
                        }else {
                            val builder = AlertDialog.Builder(this)
                            builder.setTitle("錯誤!")
                            builder.setMessage("出現錯誤，錯誤代碼: \n$e")
                            builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                            builder.show()
                        }
                    }catch (e: Throwable) {
                        val builder = AlertDialog.Builder(this)
                        builder.setTitle("錯誤!")
                        builder.setMessage("出現未知錯誤，錯誤代碼: \n$e")
                        builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                        builder.show()
                    }
                }
                else -> {
                    val builder = AlertDialog.Builder(this)
                    builder.setTitle("錯誤!")
                    builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->}
                    builder.show()
                }
            }
        }

        summonHistoryButton.setOnClickListener {
            val builder = AlertDialog.Builder(this)
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
            val builder = AlertDialog.Builder(this)
            builder.setTitle("警告")
            builder.setIconAttribute(android.R.attr.alertDialogIcon)
            builder.setMessage("這個動作會使\"生成歷史\"及\"題號\"通通歸零，將不會保存。是否繼續？")
            builder.setPositiveButton("確定") { dialogInterface: DialogInterface, i: Int ->
                qNum = 0
                QNumNumBox.text = qNum.toString()
                summonHistory = StringBuffer()
                val toast = Toast.makeText(applicationContext, "已歸零", Toast.LENGTH_SHORT)
                toast.show()
            }
            builder.setNegativeButton("取消") { dialogInterface: DialogInterface, i: Int ->}
            builder.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val i: Int = item.itemId

        if (i == R.id.action_about) {
            val builder = AlertDialog.Builder(this)
            //userInput.setText(Html.fromHtml(getResources().getString(R.string.action_about)))
            builder.setTitle("關於")
            builder.setMessage(R.string.about_string)
            builder.setCancelable(true)
            //builder.setView(R.layout.activity_about)
            builder.setPositiveButton("確定") { dialogInterface: DialogInterface, int: Int ->}
            val finalAlert: AlertDialog = builder.create()
            finalAlert.show()
            (finalAlert.findViewById(android.R.id.message) as TextView).movementMethod =
                LinkMovementMethod.getInstance()
        }

        return super.onOptionsItemSelected(item)
    }

    fun invisibleAll() {
        RandIntMax.visibility = View.INVISIBLE
        RandIntMin.visibility = View.INVISIBLE
        RandAnsFirst.visibility = View.INVISIBLE
        RandAnsLast.visibility = View.INVISIBLE
        RandAnsAll.visibility = View.INVISIBLE
        RandAnsAllHint.visibility = View.INVISIBLE
    }
}