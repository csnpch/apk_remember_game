package com.example.assign06_6306021621138

import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.provider.MediaStore.Audio.Media
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random


class OnPlay : AppCompatActivity(), View.OnClickListener {

    // Link XML
    private lateinit var containerGrid: GridLayout
    private lateinit var txt_countdownTime: TextView
    private lateinit var txt_labelTimeLeft: TextView
    private lateinit var btnGroupBottomArea: LinearLayout
    private lateinit var btn_playAgain: Button
    private lateinit var btn_backToMain: Button

    private lateinit var list_items: Array<Item>
    private var itemSelected: Array<Int> = Array(2) { -1 }
    private var indexItemSelected: Int = 0

    private var gameMode: Int = 0  // 0 easy, 1 normal, 2 hard
    private val gameConfig: List<List<Int>> = listOf(
        // totalItem, row, col, sizeBox, timer(sec)
        listOf(8, 4, 2, 80, 15),
        listOf(12, 4, 3, 60, 30),
        listOf(20, 5, 4, 40, 40)
    )
    private val itemImgResources: IntArray = intArrayOf(
        R.drawable.idontknow,
        R.drawable.ciruela,
        R.drawable.apple,
        R.drawable.apricot,
        R.drawable.banana,
        R.drawable.cherry,
        R.drawable.mango,
        R.drawable.pear,
        R.drawable.strawberry,
        R.drawable.watermalon
    )

    private var statusWin: Int = 0  // 0 is onPlay, 1 is win, -1 is lose
    private lateinit var timeLeft: CountDownTimer

    //    Sounds
    private lateinit var soundCorrect: MediaPlayer
    private lateinit var soundIncorrect: MediaPlayer
    private lateinit var soundLoser: MediaPlayer
    private lateinit var soundWinner: MediaPlayer
    private lateinit var soundBackground: MediaPlayer

    private fun setGameMode(mode: Int) { this.gameMode = mode }
    private fun getGameMode(): Int { return this.gameMode }

    private fun setStatusWin(status: Int) { this.statusWin = status }
    private fun getStatusWin(): Int { return this.statusWin }

    private fun soundControl(where: String) {
        when (where) {
            "correct" -> soundCorrect.start()
            "incorrect" -> soundIncorrect.start()
            "loser" -> soundLoser.start()
            "winner" -> soundWinner.start()
            "bg" -> soundBackground.start()
            "bg_stop" -> {
                soundBackground.pause()
                soundBackground.seekTo(0);
            }
        }
    }


    private fun init() {
        this.title = "Playing..."
        this.initIntentProp()

        this.initVariables()
        this.setupGamePlay()

        this.setupTimeLeft()
        this.initSounds()
        this.soundControl("bg")
    }


    private fun initIntentProp() {
        intent?.let {
            val gameModeString = it.getStringExtra("intent_gameMode")
            if (gameModeString != null) {
                try {
                    this.setGameMode(gameModeString.toInt())
                } catch (e: java.lang.NumberFormatException) {
                    throw error("Error : GameMode setup Failed :catch")
                }
            } else throw error("Error : GameMode setup Failed :null")
        }
    }


    private fun initVariables() {
        containerGrid = findViewById(R.id.onPlay_containerGrid)
        btnGroupBottomArea = findViewById(R.id.onPlay_btnBottomArea)

        txt_countdownTime = findViewById(R.id.txt_countdownTime)
        txt_labelTimeLeft = findViewById(R.id.txt_labelTimeLeft)

        btn_playAgain = findViewById(R.id.btn_playAgain)
        btn_playAgain.setOnClickListener(this)

        btn_backToMain = findViewById(R.id.btn_backToMain)
        btn_backToMain.setOnClickListener(this)
    }


    private fun setupTimeLeft() {
        val tmpTimeLeft = this.gameConfig[this.getGameMode()][4]
        timeLeft = object : CountDownTimer((tmpTimeLeft * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                txt_countdownTime.text = ((millisUntilFinished / 1000) + 1).toString()
            }
            override fun onFinish() {
                this@OnPlay.soundControl("bg_stop")
                this@OnPlay.soundControl("loser")
                txt_labelTimeLeft.text = ""
                txt_countdownTime.text = "YOUR LOSE"
                btnGroupBottomArea.visibility = View.VISIBLE
                this@OnPlay.setStatusWin(-1)
            }
        }
        timeLeft.start()
    }


    private fun initSounds() {
        soundCorrect = MediaPlayer.create(this, R.raw.correct)
        soundIncorrect = MediaPlayer.create(this, R.raw.incorrect)
        soundLoser = MediaPlayer.create(this, R.raw.loser)
        soundWinner = MediaPlayer.create(this, R.raw.winner)
        soundBackground = MediaPlayer.create(this, R.raw.bg_music)
    }


    private fun setupGamePlay() {

        fun setupItemImageView(id: Int, imgView: ImageView, resourceImage: Int): ImageView {
            imgView.id = id
            imgView.setImageResource(resourceImage)
            imgView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            imgView.setOnClickListener(this)
            return imgView
        }

        fun getCustomLayoutParams(sizeBox: Int): LinearLayout.LayoutParams {
            val customLayoutParams = LinearLayout.LayoutParams(sizeBox, sizeBox)
            customLayoutParams.setMargins(30, 30, 30, 30)
            return customLayoutParams
        }

        fun generateDuoValueForMatch(totalItem: Int): List<List<String>> {
            var duoValMatch: MutableList<MutableList<String>> = MutableList(totalItem) { MutableList(2) { "" } }
            var helper = Helper()

            var indexImg = 0;
            for (index in 0 until totalItem) {
                if (index % 2 == 0 && index < totalItem - 1) {
                    val valueForMatch = helper.randomString()
                    duoValMatch[index][0] = valueForMatch
                    duoValMatch[index][1] = itemImgResources[indexImg].toString()
                    duoValMatch[index + 1][0] = valueForMatch
                    duoValMatch[index + 1][1] = itemImgResources[indexImg].toString()
                    indexImg++
                }
            }

            return duoValMatch
        }

        fun swapPosItem(data: Array<Item>): Array<Item> {


            var listIndex: MutableList<Int> = mutableListOf()
            for (n in 0 until data.size) {
                listIndex.add(n)
            }

            fun randomIndex(): Int {
                var tmpIndexRand = Random.nextInt(listIndex.size)
                var resultIndex = listIndex[tmpIndexRand]
                listIndex.removeAt(tmpIndexRand)
                return resultIndex
            }

            var tmpSwap: Array<Item> = Array(data.size) {
                Item(ImageView(this), 0, "", false)
            }

            var indexRand: Int
            for (item in data) {
                indexRand = randomIndex()
                tmpSwap[indexRand] = item
            }

            return tmpSwap
        }


        // Main of setupGamePlay
        val config: List<Int> = this.gameConfig[this.getGameMode()]
        val totalItem = config[0]
        val row = config[1]
        val col = config[2]
        val sizeBox = config[3]
        val valueDuoMatch: List<List<String>> = generateDuoValueForMatch(totalItem)
        var indexItem = 0

        if ((totalItem / 2) > itemImgResources.size) throw Error("ResourceImages < itemsGamePlay")

        list_items = Array(totalItem) { Item(ImageView(this), 0, "", false) }
        containerGrid.columnCount = col

        for (r in 0 until row) {

            for (c in 0 until col) {
                println(valueDuoMatch[r])
                list_items[indexItem].valueMatch = valueDuoMatch[indexItem][0]
                list_items[indexItem].drawableSource = valueDuoMatch[indexItem][1].toInt()
                list_items[indexItem].imgView = setupItemImageView(
                    2000 + indexItem,
                    list_items[indexItem].imgView,
                    R.drawable.hidden
                )
                indexItem++;
            }

        }

        this.list_items = swapPosItem(this.list_items)
        for (item in this.list_items) {
            containerGrid.addView(item.imgView, getCustomLayoutParams(sizeBox + 120))
        }

    }


    private fun findIndexItemClick(view: View?): Int {
        var id = view?.id
        for (index in list_items.indices)
            if (id == list_items[index].imgView.id)
                return index
        return -1
    }


    private fun findItemByImgId(id: Int): Int {
        for (index in list_items.indices)
            if (id == list_items[index].imgView.id)
                return index
        return -1
    }


    private fun checkItemSelectedIsExist(id: Int): Int {
        if (this.itemSelected[0] == id)
            return 0
        else if (this.itemSelected[1] == id)
            return 1
        return -1
    }


    private fun resetItemSelected() {
        this.itemSelected[0] = -1
        this.itemSelected[1] = -1
        indexItemSelected = 0
    }


    private fun resetTimeLeft() {
        txt_labelTimeLeft.text = "Time left : "
        txt_countdownTime.setTextColor(Color.parseColor("#FF2600"))
    }


    private fun checkUserWin() {
        this.setStatusWin(1)

        for (item in list_items) {
            if (!item.statusFound) {
                this.setStatusWin(0)
                break
            }
        }

        if (this.getStatusWin() == 1) {
            this@OnPlay.soundControl("bg_stop")
            this@OnPlay.soundControl("winner")
            timeLeft.cancel()
            btnGroupBottomArea.visibility = View.VISIBLE
            txt_labelTimeLeft.text = ""
            txt_countdownTime.setTextColor(Color.parseColor("#1e9600"))
            txt_countdownTime.text = "✨ YOUR WINNER 🎉"
            Toast.makeText(this, "YOUR WINNER ✨🎉", Toast.LENGTH_SHORT).show()
        }
    }


    private fun validateMatch() {

        fun onInvalid() {
            for (item in list_items) {
                if (!item.statusFound)
                    item.setHidden(false)
            }
            this.resetItemSelected()
            this.soundControl("incorrect")
        }

        // Main of validate match
        if (this.itemSelected[0] == -1 || this.itemSelected[1] == -1) return

        val idxItem1 = this.findItemByImgId(itemSelected[0])
        val idxItem2 = this.findItemByImgId(itemSelected[1])

        if (list_items[idxItem1].valueMatch != list_items[idxItem2].valueMatch)
            onInvalid()
        else if (list_items[idxItem1].valueMatch == list_items[idxItem2].valueMatch) {
            this.soundControl("correct")
            list_items[idxItem1].statusFound = true
            list_items[idxItem2].statusFound = true
            this.resetItemSelected()
        }
        this.checkUserWin()

        println("valueMatch [0](${list_items[idxItem1].valueMatch}) : [2](${list_items[idxItem2].valueMatch})")

    }


    private fun onClickItem(view: View?) {

        if (this.getStatusWin() == 1 || this.getStatusWin() == -1) return

        var index = this.findIndexItemClick(view);
        if (index == -1) Toast.makeText(this, "Error: Not found event", Toast.LENGTH_SHORT).show()

        var item = list_items[index]
        val checkPosElement = this.checkItemSelectedIsExist(item.imgView.id)

        if (item.statusFound) return

        if (indexItemSelected < 2 && this.checkItemSelectedIsExist(item.imgView.id) == -1) {

            if (itemSelected[0] != -1 && itemSelected[1] != -1) return

            item.toggleHidden()
            itemSelected[indexItemSelected] = item.imgView.id
            if (indexItemSelected < 1) indexItemSelected++

        } else if (indexItemSelected > 0 && this.checkItemSelectedIsExist(item.imgView.id) != -1) {

            item.toggleHidden()
            if (checkPosElement == 1) {
                itemSelected[checkPosElement] = -1
                indexItemSelected = 1
            } else if (checkPosElement == 0) {
                itemSelected[checkPosElement] = -1
                indexItemSelected = 0
            }

        }
        println("valueSelected [0](${itemSelected[0]}) : [1](${itemSelected[1]})")

        val delayValidate = object : CountDownTimer(500L, 1000) {
            override fun onTick(millisUntilFinished: Long) { }
            override fun onFinish() {
                this@OnPlay.validateMatch()
            }
        }.start()

    }


    private fun clearItems() {
        for (item in list_items) {
            item.setHidden(false)
            item.setFound(false)
        }
    }


    private fun onRestartGame() {
        this.setStatusWin(0)
        this.resetTimeLeft()
        this.clearItems()
        btnGroupBottomArea.visibility = View.INVISIBLE
        timeLeft.start()
        this.soundControl("bg")
    }


    override fun onClick(view: View?) {
        if (view?.id == btn_playAgain.id)
            this.onRestartGame()
        else if (view?.id == btn_backToMain.id)
            this.finish()
        else
            this.onClickItem(view)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_play)

        this.init()
    }
}