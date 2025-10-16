package dev.suchbyte.openSurvivalGames.domain.cosmetics

enum class Cosmetics(val uid: Int, val displayName: String? = null) {
    SGMySword(2000, "My Sword"),
    SGDeathCrate(2001, "Death Crate"),
    SGHorseDeathBattleCry(2002, "Horse Death"),
    SGLevelUpBattleCry(2003, "Level Up"),
    SGWitherDeathBattleCry(2004, "Wither Death"),
    SGIronGolemDeathBattleCry(2005, "Iron Golem Death"),
    SGBlazeDeathBattleCry(2006, "Blaze Death"),
    SGWitherHurtBattleCry(2007, "Wither Hurt"),
    SGPlayerBurpBattleCry(2008, "Player Burp"),
    SGExplosionBattleCry(2009, "Explosion"),
    SGEnderDragonHurtBattleCry(2010, "Ender Dragon Hurt"),
    SGZombieDoorBreakBattleCry(2011, "Zombie Door Break"),
    SGExperienceOrbPickupBattleCry(2012, "Experience Orb Pickup"),
    SGWolfHowlBattleCry(2013, "Wolf Howl"),
    SGPigDeathBattleCry(2014, "Pig Death"),
    SGFlamesArrowTrail(2015, "Flames"),
    SGHeartsArrowTrail(2016, "Hearts"),
    SGNotesArrowTrail(2017, "Notes"),
    SGColoredDustArrowTrail(2018, "Colored Dust"),
    SGHappyVillagerArrowTrail(2019, "Happy Villager"),
    SGLavaDripArrowTrail(2020, "Lava Drip"),
    SGLavaPopArrowTrail(2021, "Lava Pop"),
    SGVillagerThundercloudArrowTrail(2022, "Thundercloud")
}