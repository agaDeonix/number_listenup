package com.pinkunicorp.voicenumbers.extentions

import com.pinkunicorp.voicenumbers.ui.screens.training.getMediaItem

fun Long.toWholeNumberString(): String {
    val listMediaPlayer = mutableListOf<String?>()
    val billion = this / 1000000000
    val million = (this - billion * 1000000000) / 1000000
    val thousand = (this - billion * 1000000000 - million * 1000000) / 1000
    val hundred =
        (this - billion * 1000000000 - million * 1000000 - thousand * 1000) / 100
    val ten = if (this % 100 <= 20) 0 else
        (this - billion * 1000000000 - million * 1000000 - thousand * 1000 - hundred * 100) / 10
    val one = if (this % 100 <= 20) this % 100 else
        this - billion * 1000000000 - million * 1000000 - thousand * 1000 - hundred * 100 - ten * 10

    if (billion > 0) {
        val billionHundreds = billion / 100
        val billionTens =
            if (billion % 100 <= 20) 0 else (billion - billionHundreds * 100) / 10
        val billionOnes =
            if (billion % 100 <= 20) billion % 100 else billion - billionHundreds * 100 - billionTens * 10

        if (billionHundreds > 0) {
            listMediaPlayer.add(
                getMediaItem(
                    billionHundreds.toString()
                )
            )
            listMediaPlayer.add(getMediaItem("hundred"))
        }
        if (billionTens > 0) {
            listMediaPlayer.add(
                getMediaItem(
                    (billionTens * 10).toString()
                )
            )
        }
        if (billionOnes > 0) {
            listMediaPlayer.add(getMediaItem(billionOnes.toString()))
        }
        listMediaPlayer.add(getMediaItem("billion"))
    }
    if (million > 0) {
        val millionHundreds = million / 100
        val millionTens =
            if (million % 100 <= 20) 0 else (million - millionHundreds * 100) / 10
        val millionOnes =
            if (million % 100 <= 20) million % 100 else million - millionHundreds * 100 - millionTens * 10

        if (millionHundreds > 0) {
            listMediaPlayer.add(
                getMediaItem(
                    millionHundreds.toString()
                )
            )
            listMediaPlayer.add(getMediaItem("hundred"))
        }
        if (millionTens > 0) {
            listMediaPlayer.add(
                getMediaItem(
                    (millionTens * 10).toString()
                )
            )
        }
        if (millionOnes > 0) {
            listMediaPlayer.add(getMediaItem(millionOnes.toString()))
        }
        listMediaPlayer.add(getMediaItem("million"))
    }
    if (thousand > 0) {
        val thousandHundreds = thousand / 100
        val thousandTens =
            if (thousand % 100 <= 20) 0 else (thousand - thousandHundreds * 100) / 10
        val thousandOnes =
            if (thousand % 100 <= 20) thousand % 100 else thousand - thousandHundreds * 100 - thousandTens * 10

        if (thousandHundreds > 0) {
            listMediaPlayer.add(
                getMediaItem(
                    thousandHundreds.toString()
                )
            )
            listMediaPlayer.add(getMediaItem("hundred"))
        }
        if (thousandTens > 0) {
            listMediaPlayer.add(
                getMediaItem(
                    (thousandTens * 10).toString()
                )
            )
        }
        if (thousandOnes > 0) {
            listMediaPlayer.add(getMediaItem(thousandOnes.toString()))
        }
        listMediaPlayer.add(getMediaItem("thousand"))
    }
    if (hundred > 0) {
        listMediaPlayer.add(getMediaItem(hundred.toString()))
        listMediaPlayer.add(getMediaItem("hundred"))
    }
    if (ten > 0) {
        listMediaPlayer.add(getMediaItem((ten * 10).toString()))
    }
    if (one > 0) {
        listMediaPlayer.add(getMediaItem(one.toString()))
    }
    return listMediaPlayer.filterNotNull().joinToString(" ")
}

fun Long.toOrdinalNumberString(): String {
    //FIXME need rework
    val listMediaPlayer = mutableListOf<String?>()
    val billion = this / 1000000000
    val million = (this - billion * 1000000000) / 1000000
    val thousand = (this - billion * 1000000000 - million * 1000000) / 1000
    val hundred =
        (this - billion * 1000000000 - million * 1000000 - thousand * 1000) / 100
    val ten = if (this % 100 <= 20) 0 else
        (this - billion * 1000000000 - million * 1000000 - thousand * 1000 - hundred * 100) / 10
    val one = if (this % 100 <= 20) this % 100 else
        this - billion * 1000000000 - million * 1000000 - thousand * 1000 - hundred * 100 - ten * 10

    if (billion > 0) {
        val billionHundreds = billion / 100
        val billionTens =
            if (billion % 100 <= 20) 0 else (billion - billionHundreds * 100) / 10
        val billionOnes =
            if (billion % 100 <= 20) billion % 100 else billion - billionHundreds * 100 - billionTens * 10

        if (billionHundreds > 0) {
            listMediaPlayer.add(
                getMediaItem(
                    billionHundreds.toString()
                )
            )
            listMediaPlayer.add(getMediaItem("hundred"))
        }
        if (billionTens > 0) {
            listMediaPlayer.add(
                getMediaItem(
                    (billionTens * 10).toString()
                )
            )
        }
        if (billionOnes > 0) {
            listMediaPlayer.add(getMediaItem(billionOnes.toString()))
        }
        listMediaPlayer.add(getMediaItem("billion"))
    }
    if (million > 0) {
        val millionHundreds = million / 100
        val millionTens =
            if (million % 100 <= 20) 0 else (million - millionHundreds * 100) / 10
        val millionOnes =
            if (million % 100 <= 20) million % 100 else million - millionHundreds * 100 - millionTens * 10

        if (millionHundreds > 0) {
            listMediaPlayer.add(
                getMediaItem(
                    millionHundreds.toString()
                )
            )
            listMediaPlayer.add(getMediaItem("hundred"))
        }
        if (millionTens > 0) {
            listMediaPlayer.add(
                getMediaItem(
                    (millionTens * 10).toString()
                )
            )
        }
        if (millionOnes > 0) {
            listMediaPlayer.add(getMediaItem(millionOnes.toString()))
        }
        listMediaPlayer.add(getMediaItem("million"))
    }
    if (thousand > 0) {
        val thousandHundreds = thousand / 100
        val thousandTens =
            if (thousand % 100 <= 20) 0 else (thousand - thousandHundreds * 100) / 10
        val thousandOnes =
            if (thousand % 100 <= 20) thousand % 100 else thousand - thousandHundreds * 100 - thousandTens * 10

        if (thousandHundreds > 0) {
            listMediaPlayer.add(
                getMediaItem(
                    thousandHundreds.toString()
                )
            )
            listMediaPlayer.add(getMediaItem("hundred"))
        }
        if (thousandTens > 0) {
            listMediaPlayer.add(
                getMediaItem(
                    (thousandTens * 10).toString()
                )
            )
        }
        if (thousandOnes > 0) {
            listMediaPlayer.add(getMediaItem(thousandOnes.toString()))
        }
        listMediaPlayer.add(getMediaItem("thousand"))
    }
    if (hundred > 0) {
        listMediaPlayer.add(getMediaItem(hundred.toString()))
        listMediaPlayer.add(getMediaItem("hundred"))
    }
    if (ten > 0) {
        listMediaPlayer.add(getMediaItem((ten * 10).toString()))
    }
    if (one > 0) {
        listMediaPlayer.add(getMediaItem(one.toString()))
    }
    return listMediaPlayer.filterNotNull().joinToString(" ")
}