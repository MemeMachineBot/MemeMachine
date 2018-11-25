package me.kavin.mememachine.command.commands;

import java.util.Random;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import me.kavin.mememachine.command.Command;
import me.kavin.mememachine.utils.ColorUtils;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class UselessWeb extends Command {

	private static final Random RANDOM = new Random();
	private ObjectArrayList<String> urls = new ObjectArrayList<>();

	public UselessWeb() {
		super(">uselessweb", "`Shows a random useless website! (from theuselessweb.com)`");
		urls.add("http://heeeeeeeey.com/");
		urls.add("http://tinytuba.com/");
		urls.add("http://corndog.io/");
		urls.add("http://thatsthefinger.com/");
		urls.add("http://cant-not-tweet-this.com/");
		urls.add("http://weirdorconfusing.com/");
		urls.add("http://eelslap.com/");
		urls.add("http://www.staggeringbeauty.com/");
		urls.add("http://burymewithmymoney.com/");
		urls.add("http://endless.horse/");
		urls.add("http://www.fallingfalling.com/");
		urls.add("http://www.trypap.com/");
		urls.add("http://www.republiquedesmangues.fr/");
		urls.add("http://www.movenowthinklater.com/");
		urls.add("http://www.partridgegetslucky.com/");
		urls.add("http://www.rrrgggbbb.com/");
		urls.add("http://beesbeesbees.com/");
		urls.add("http://www.sanger.dk/");
		urls.add("http://www.koalastothemax.com/");
		urls.add("http://www.everydayim.com/");
		urls.add("http://www.leduchamp.com/");
		urls.add("http://r33b.net/");
		urls.add("http://randomcolour.com/");
		urls.add("http://cat-bounce.com/");
		urls.add("http://www.sadforjapan.com/");
		urls.add("http://isitwednesdaymydudes.ml/");
		urls.add("http://metaphorsofinfinity.com/");
		urls.add("http://chrismckenzie.com/");
		urls.add("http://hasthelargehadroncolliderdestroyedtheworldyet.com/");
		urls.add("http://ninjaflex.com/");
		urls.add("http://iloveyoulikeafatladylovesapples.com/");
		urls.add("http://ihasabucket.com/");
		urls.add("http://corndogoncorndog.com/");
		urls.add("https://pointerpointer.com");
		urls.add("http://imaninja.com/");
		urls.add("http://www.ismycomputeron.com/");
		urls.add("http://www.nullingthevoid.com/");
		urls.add("http://www.muchbetterthanthis.com/");
		urls.add("http://www.yesnoif.com/");
		urls.add("http://iamawesome.com/");
		urls.add("http://www.pleaselike.com/");
		urls.add("http://crouton.net/");
		urls.add("http://corgiorgy.com/");
		urls.add("http://www.electricboogiewoogie.com/");
		urls.add("http://www.wutdafuk.com/");
		urls.add("http://unicodesnowmanforyou.com/");
		urls.add("http://www.crossdivisions.com/");
		urls.add("http://tencents.info/");
		urls.add("http://intotime.com/");
		urls.add("http://leekspin.com/");
		urls.add("http://www.patience-is-a-virtue.org/");
		urls.add("http://whitetrash.nl/");
		urls.add("http://www.theendofreason.com/");
		urls.add("http://zombo.com");
		urls.add("http://pixelsfighting.com/");
		urls.add("http://baconsizzling.com/");
		urls.add("http://isitwhite.com/");
		urls.add("http://onemillionlols.com/");
		urls.add("http://www.omfgdogs.com/");
		urls.add("http://oct82.com/");
		urls.add("http://semanticresponsiveillustration.com/");
		urls.add("http://chihuahuaspin.com/");
		urls.add("http://www.blankwindows.com/");
		urls.add("http://www.biglongnow.com/");
		urls.add("http://dogs.are.the.most.moe/");
		urls.add("http://tunnelsnakes.com/");
		urls.add("http://www.infinitething.com/");
		urls.add("http://www.trashloop.com/");
		urls.add("http://www.ascii-middle-finger.com/");
		urls.add("http://www.coloursquares.com/");
		urls.add("http://spaceis.cool/");
		urls.add("http://buildshruggie.com/");
		urls.add("http://buzzybuzz.biz/");
		urls.add("http://yeahlemons.com/");
		urls.add("http://burnie.com/");
		urls.add("http://wowenwilsonquiz.com");
	}

	@Override
	public void onCommand(String message, MessageReceivedEvent event) {
		EmbedBuilder meb = new EmbedBuilder();

		meb.setTitle("Useless Web: ");
		meb.setColor(ColorUtils.getRainbowColor(2000));

		meb.setDescription("Click [here](" + urls.get(RANDOM.nextInt(urls.size())) + ")");

		event.getChannel().sendMessage(meb.build()).complete();
	}

}