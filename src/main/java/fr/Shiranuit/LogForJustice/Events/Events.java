package fr.Shiranuit.LogForJustice.Events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import fr.Shiranuit.LogForJustice.Main;
import fr.Shiranuit.LogForJustice.Area.Area;
import fr.Shiranuit.LogForJustice.Area.Flag;
import fr.Shiranuit.LogForJustice.Area.FlagType;
import fr.Shiranuit.LogForJustice.Area.Selection;
import fr.Shiranuit.LogForJustice.Fake.AlterateCommand;
import fr.Shiranuit.LogForJustice.Grade.RankInfo;
import fr.Shiranuit.LogForJustice.LogForJustice.BlockInfos;
import fr.Shiranuit.LogForJustice.LogForJustice.EnumPlayerAction;
import fr.Shiranuit.LogForJustice.Manager.AreaManager;
import fr.Shiranuit.LogForJustice.Manager.ChunkManager;
import fr.Shiranuit.LogForJustice.Manager.CommandSpyManager;
import fr.Shiranuit.LogForJustice.Manager.RankManager;
import fr.Shiranuit.LogForJustice.Manager.ToolManager;
import fr.Shiranuit.LogForJustice.PlayerData.NBTPlayerData;
import fr.Shiranuit.LogForJustice.PlayerData.Position;
import fr.Shiranuit.LogForJustice.Manager.LogForJusticeManager;
import fr.Shiranuit.LogForJustice.Manager.MessageSpyManager;
import fr.Shiranuit.LogForJustice.Manager.PlayerManager;
import fr.Shiranuit.LogForJustice.Utils.ModLoaderUtil;
import fr.Shiranuit.LogForJustice.Utils.Util;
import fr.Shiranuit.LogForJustice.Utils.Chat.ChatType;
import fr.Shiranuit.LogForJustice.Utils.Chat.ChatUtil;
import net.minecraft.advancements.PlayerAdvancements;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockEnderChest;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandHandler;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.network.EnumConnectionState;
import net.minecraft.network.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.ServerChatEvent;

public class Events {
	
	@SubscribeEvent
	public void onDeath(LivingDeathEvent e) {
		if (e.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)e.getEntityLiving();
			if (RankManager.hasPermission("back", "death", "", -1, player)) {
				PlayerManager.previousPositions.put(player.getGameProfile().getId().toString(), new Position(player.getPositionVector(), player.dimension));
			}
		}
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onBlockPlaced(BlockEvent.PlaceEvent e) {
		if (Main.enabled) {
			if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
				EntityPlayer player = e.getPlayer();
				IBlockState iblock = e.getPlacedBlock();
				Block block = iblock.getBlock();
				if (!(e.getPlayer() instanceof FakePlayer)) {
					Area zone = AreaManager.getArea(e.getWorld(), e.getPos());
					Flag flag = AreaManager.getFlag(zone, "place");
					if (!flag.isNull()) {
						if (flag.isEqual(false) && !(AreaManager.isOwner(zone,e.getPlayer()) || AreaManager.isMember(zone,e.getPlayer()) || AreaManager.hasBypass(e.getPlayer()))) {
							e.setCanceled(true);
							e.getPlayer().sendMessage(new TextComponentString(TextFormatting.RED+"You don't have the permission to place blocks in this area"));
							return;	
						}
					}
					
					int meta = block.getMetaFromState(iblock);
					if (!RankManager.hasPermission("place", block.getRegistryName().getResourceDomain(),block.getRegistryName().getResourcePath(), meta, player)) {
						TextComponentString txt = new TextComponentString(TextFormatting.RED+"You don't have the permission to place this block");
						player.sendMessage(txt);
						e.setCanceled(true);
						return;
					}
				}
				
				String maDate = Util.getDate();
				BlockInfos info = new BlockInfos(EnumPlayerAction.PLACE,player.getDisplayNameString(), Block.getIdFromBlock(block), block.getMetaFromState(iblock), -1, 0, maDate);
				LogForJusticeManager.addLog(player.dimension, e.getPos(), info, true);
			}
		}
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onBlockBreak(BlockEvent.BreakEvent e)  {
		if (Main.enabled) {
			if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
				EntityPlayer player = e.getPlayer();
				IBlockState iblock = e.getState();
				Block block = iblock.getBlock();
				ItemStack hand = player.getHeldItemMainhand();
				Item ihand = hand.getItem();
				int meta = ihand.getDamage(hand);
				if (!(e.getPlayer() instanceof FakePlayer)) {
					Area zone = AreaManager.getArea(e.getWorld(), e.getPos());
					Flag flag = AreaManager.getFlag(zone, "break");
					if (!flag.isNull()) {
						if (flag.isEqual(false) && !(AreaManager.isOwner(zone,e.getPlayer()) || AreaManager.isMember(zone,e.getPlayer()) || AreaManager.hasBypass(e.getPlayer()))) {
							e.getPlayer().sendMessage(new TextComponentString(TextFormatting.RED+"You don't have the permission to break blocks in this area"));
							e.setCanceled(true);
							return;	
						}
					}
	
					if (!RankManager.hasPermission("break", block.getRegistryName().getResourceDomain(),block.getRegistryName().getResourcePath(), meta, player)) {
						TextComponentString txt = new TextComponentString(TextFormatting.RED+"You don't have the permission to break this block");
						player.sendMessage(txt);
						e.setCanceled(true);
						return;
					}
				}
				
				String maDate = Util.getDate();
				BlockInfos info = new BlockInfos(EnumPlayerAction.BREAK,player.getDisplayNameString(), Block.getIdFromBlock(block), block.getMetaFromState(iblock), Item.getIdFromItem(ihand), ihand.getMetadata(hand), maDate);
				LogForJusticeManager.addLog(player.dimension, e.getPos(), info, true);
			}
		}
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onPlayerInteract(PlayerInteractEvent e) {
		if (Main.enabled) {
			if (e.getEntity() instanceof EntityPlayer && !(e.getEntity() instanceof FakePlayer)) {
				if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
					if (e instanceof PlayerInteractEvent.RightClickBlock || e instanceof PlayerInteractEvent.LeftClickBlock) {
						Area zone = AreaManager.getArea(e.getWorld(), e.getPos());
						Flag flag = AreaManager.getFlag(zone, "interact");
						if (!flag.isNull()) {
							if (flag.isEqual(false) && !(AreaManager.isOwner(zone,e.getEntity()) || AreaManager.isMember(zone,e.getEntity()) || AreaManager.hasBypass(e.getEntityPlayer()))) {
								e.getEntityPlayer().sendMessage(new TextComponentString(TextFormatting.RED+"You don't have the permission to interact blocks in this area"));
								e.setCanceled(true);
								return;	
							}
						}
					} else if (e instanceof PlayerInteractEvent.RightClickItem) {
						Area zone = AreaManager.getArea(e.getWorld(), e.getPos());
						Flag flag = AreaManager.getFlag(zone, "use");
						if (!flag.isNull()) {
							if (flag.isEqual(false) && !(AreaManager.isOwner(zone,e.getEntity()) || AreaManager.isMember(zone,e.getEntity()) || AreaManager.hasBypass(e.getEntityPlayer()))) {
								e.getEntityPlayer().sendMessage(new TextComponentString(TextFormatting.RED+"You don't have the permission to use items in this area"));
								e.setCanceled(true);
								return;	
							}
						}
					} else if (e instanceof PlayerInteractEvent.EntityInteract) {
						Area zone = AreaManager.getArea(e.getWorld(), e.getPos());
						Flag flag = AreaManager.getFlag(zone, "interact-entity");
						if (!flag.isNull()) {
							if (flag.isEqual(false) && !(AreaManager.isOwner(zone,e.getEntity()) || AreaManager.isMember(zone,e.getEntity()) || AreaManager.hasBypass(e.getEntityPlayer()))) {
								e.getEntityPlayer().sendMessage(new TextComponentString(TextFormatting.RED+"You don't have the permission to interact entities in this area"));
								e.setCanceled(true);
								return;	
							}
						}
					}
					
					IBlockState iblock = e.getWorld().getBlockState(e.getPos());
					Block block = iblock.getBlock();
					int meta = block.getMetaFromState(iblock);
					EntityPlayer player = (EntityPlayer)e.getEntity();
					int playerLevel = RankManager.getPlayerPower(e.getEntityPlayer());
					if (e instanceof PlayerInteractEvent.EntityInteract) {
						PlayerInteractEvent.EntityInteract f = (PlayerInteractEvent.EntityInteract)e;
						if (!RankManager.hasPermission("interact-entity", f.getTarget().getName(),"", -1, player)) {
							TextComponentString txt = new TextComponentString(TextFormatting.RED+"You don't have the permission to interact with this entity");
							player.sendMessage(txt);
							e.setCanceled(true);
							return;
						}
					}
					if (e instanceof PlayerInteractEvent.RightClickBlock) {
						if (!RankManager.hasPermission("interact", block.getRegistryName().getResourceDomain(),block.getRegistryName().getResourcePath(), meta, player)) {
							TextComponentString txt = new TextComponentString(TextFormatting.RED+"You don't have the permission to interact with this block");
							player.sendMessage(txt);
							e.setCanceled(true);
							return;
						}
					}
					if (e.getHand() == EnumHand.MAIN_HAND || e.getEntityPlayer().getHeldItem(e.getHand()) != ItemStack.EMPTY) {
						ItemStack hand = player.getHeldItem(e.getHand());
						Item ihand = hand.getItem();
						int imeta = ihand.getDamage(hand);
						if (e instanceof PlayerInteractEvent.RightClickItem) {
							if (!RankManager.hasPermission("use", ihand.getRegistryName().getResourceDomain(),ihand.getRegistryName().getResourcePath(), imeta, player)) {
								TextComponentString txt = new TextComponentString(TextFormatting.RED+"You don't have the permission to use this item");
								player.sendMessage(txt);
								e.setCanceled(true);
								return;
							}
						}
						String name = ihand.getRegistryName().getResourceDomain() + ":" + ihand.getRegistryName().getResourcePath();
						if (name.equals(ToolManager.LogForJusticeToolID) && Util.isOp(e.getEntityPlayer()) && e instanceof PlayerInteractEvent.RightClickBlock) {
							
							ITextComponent text = new TextComponentString(TextFormatting.DARK_AQUA+"Block changes at ["+e.getPos().getX()+", "+e.getPos().getY()+", "+e.getPos().getZ()+"] in dimension ["+player.dimension+"]");
							player.sendMessage(text);
							HashMap<String, List<BlockInfos>> log = LogForJusticeManager.LFJ.get(player.dimension);
							if (log != null) {
								List<BlockInfos> data = log.get(Util.BlockPosToString(e.getPos()));
								if (data != null && data.size() > 0) {
									for (BlockInfos binfo : data) {	
										if (binfo.action == EnumPlayerAction.PLACE) {
											String blockname = Block.REGISTRY.getNameForObject(Block.getBlockById(binfo.id)) + "/" + binfo.meta;
											text = new TextComponentString(TextFormatting.GOLD+binfo.date+" "+TextFormatting.RED+binfo.playername+TextFormatting.DARK_AQUA+" created "+TextFormatting.DARK_GREEN+blockname);
											player.sendMessage(text);
										}
										if (binfo.action == EnumPlayerAction.BREAK) {
											String blockname = Block.REGISTRY.getNameForObject(Block.getBlockById(binfo.id)) + "/" + binfo.meta;
											String itemname = Item.REGISTRY.getNameForObject(Item.getItemById(binfo.itemID)) + "/" + binfo.itemMeta;
											text = new TextComponentString(TextFormatting.GOLD+binfo.date+" "+TextFormatting.RED+binfo.playername+TextFormatting.DARK_AQUA+" breaked "+TextFormatting.DARK_GREEN+blockname + TextFormatting.GOLD+" with "+TextFormatting.DARK_GREEN + itemname);
											player.sendMessage(text);
										}
										if (binfo.action == EnumPlayerAction.INTERACT) {
											String blockname = Block.REGISTRY.getNameForObject(Block.getBlockById(binfo.id)) + "/" + binfo.meta;
											String itemname = Item.REGISTRY.getNameForObject(Item.getItemById(binfo.itemID)) + "/" + binfo.itemMeta;
											text = new TextComponentString(TextFormatting.GOLD+binfo.date+" "+TextFormatting.RED+binfo.playername+TextFormatting.DARK_AQUA+" interacted "+TextFormatting.DARK_GREEN+blockname + TextFormatting.GOLD+" with "+TextFormatting.DARK_GREEN + itemname);
											player.sendMessage(text);
										}
									}
								} else {
									text = new TextComponentString(TextFormatting.DARK_AQUA+"No results found.");
									player.sendMessage(text);
								}
								
							}
						} else if (name.equals(ToolManager.RegionToolID) && Util.isOp(e.getEntityPlayer())) {
							if (e instanceof PlayerInteractEvent.LeftClickBlock) {
								Selection selection = AreaManager.getSelection(e.getEntityPlayer());
								selection.setStart(e.getPos());
								if (selection.isSet()) {
									ChatUtil.sendMessage(e.getEntityPlayer(), "First position set to ("+e.getPos().getX()+", "+e.getPos().getY()+", "+e.getPos().getZ()+") ("+selection.totalBlocks()+")", ChatType.Region);
								} else {
									ChatUtil.sendMessage(e.getEntityPlayer(), "First position set to ("+e.getPos().getX()+", "+e.getPos().getY()+", "+e.getPos().getZ()+")", ChatType.Region);
								}
								EventsOther.tickParticle.put(e.getEntityPlayer().getName(), 81);
								e.setCanceled(true);
								
							} else if (e instanceof PlayerInteractEvent.RightClickBlock) {
								Selection selection = AreaManager.getSelection(e.getEntityPlayer());
								selection.setEnd(e.getPos());
								if (selection.isSet()) {
									ChatUtil.sendMessage(e.getEntityPlayer(), "Second position set to ("+e.getPos().getX()+", "+e.getPos().getY()+", "+e.getPos().getZ()+") ("+selection.totalBlocks()+")", ChatType.Region);
								} else {
									ChatUtil.sendMessage(e.getEntityPlayer(), "Second position set to ("+e.getPos().getX()+", "+e.getPos().getY()+", "+e.getPos().getZ()+")", ChatType.Region);
								}
								EventsOther.tickParticle.put(e.getEntityPlayer().getName(), 81);
								e.setCanceled(true);
							}
						} else {
							String maDate = Util.getDate();
							BlockInfos info = new BlockInfos(EnumPlayerAction.INTERACT, player.getDisplayNameString(), Block.getIdFromBlock(block), block.getMetaFromState(iblock), Item.getIdFromItem(ihand), ihand.getMetadata(hand), maDate);
							LogForJusticeManager.addLog(player.dimension, e.getPos(), info, true);
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onCommand(CommandEvent e) {
		if (Main.enabled) {
			if (!(e.getSender() instanceof MinecraftServer || e.getSender() instanceof CommandBlockBaseLogic || !(e.getSender().getCommandSenderEntity() instanceof EntityPlayer))) {
				Area zone = AreaManager.getArea(e.getSender().getCommandSenderEntity());
				Flag flag = AreaManager.getFlag(zone, "can_use_command");
				if (!flag.isNull()) {
					if (flag.isEqual(false) && !(AreaManager.isOwner(zone,e.getSender().getCommandSenderEntity()) || AreaManager.isMember(zone,e.getSender().getCommandSenderEntity()) || Util.isOp(e.getSender().getName()))) {
						e.setCanceled(true);
						e.getSender().sendMessage(new TextComponentString(TextFormatting.RED+"You don't have the permission to use commands in this area"));
						return;	
					}
				}
				if (e.getCommand().getName().equals("tell")) {
					MessageSpyManager.sendWhisper(e.getCommand(),e.getSender(),e.getParameters());
					MessageSpyManager.add(e.getCommand(),e.getParameters(),e.getSender());
				} else if (e.getCommand().getName().equals("ban") || e.getCommand().getName().equals("deop") || e.getCommand().getName().equals("kick") || e.getCommand().getName().equals("tempban") || e.getCommand().getName().equals("mute") || e.getCommand().getName().equals("invsee")) {
					if (e.getParameters().length > 0) {
						if (RankManager.getPlayerPower(e.getSender().getName()) > RankManager.getPlayerPower(e.getParameters()[0])) {
							CommandSpyManager.sendCommand(e.getCommand(),e.getSender(),e.getParameters(),true);
							CommandSpyManager.add(e.getCommand(), e.getParameters(), e.getSender(), true);
						} else {
							CommandSpyManager.sendCommand(e.getCommand(),e.getSender(),e.getParameters(), false);
							CommandSpyManager.add(e.getCommand(), e.getParameters(), e.getSender(), false);
							TextComponentString text = new TextComponentString(TextFormatting.RED+"You can't use this commands on "+e.getParameters()[0]);
							e.getSender().sendMessage(text);
							e.setCanceled(true);
						}
					}
				} else {
					CommandSpyManager.sendCommand(e.getCommand(),e.getSender(),e.getParameters(),true);
					CommandSpyManager.add(e.getCommand(), e.getParameters(), e.getSender(), true);
				}
			} else {
				if (e.getCommand().getName().equals("list")) {
					//e.setCanceled(true);
				}
			}
		}
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onItemPickup(EntityItemPickupEvent e){
		if (Main.enabled) {
			if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
				if (e.getEntityPlayer() != null && !(e.getEntityPlayer() instanceof FakePlayer)) {
					ItemStack stack = e.getItem().getItem();
					Item item = stack.getItem();
					int itemmeta = item.getDamage(stack);
					if (!RankManager.hasPermission("itempickup", item.getRegistryName().getResourceDomain(),item.getRegistryName().getResourcePath(),itemmeta,e.getEntityPlayer())) {
						e.setCanceled(true);
						return;
					}
					if (Main.VanishedPlayers.contains(e.getEntityPlayer().getName())) {
						e.setCanceled(true);
						return;
					}
				}
				
				Area zone = AreaManager.getArea(e.getEntity());
				Flag flag = AreaManager.getFlag(zone, "itempickup");
				if (!flag.isNull()) {
					if (flag.isEqual(false) && !(AreaManager.isOwner(zone,e.getEntity()) || AreaManager.isMember(zone,e.getEntity()) || AreaManager.hasBypass(e.getEntityPlayer()))) {
						e.setCanceled(true);
						return;	
					}
				}
			}
		}
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onVisibility(PlayerEvent.Visibility e) {
		if (Main.enabled) {
			if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
				if (e.getEntityPlayer() != null) {
					if (Main.VanishedPlayers.contains(e.getEntityPlayer().getName())) {
						e.modifyVisibility(0);
					} else {
						String rank = RankManager.rankName(e.getEntityPlayer());
						if (rank != null && rank.equals("default") && RankManager.invulnerable){
							e.modifyVisibility(0);
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onEntityTakeDamage(LivingHurtEvent e) {
		if (Main.enabled) {
			if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
				if (e.getEntity() instanceof EntityPlayer && !(e.getEntity() instanceof FakePlayer)) {
					String rank = RankManager.rankName((EntityPlayer)e.getEntity());
					if (rank != null && rank.equals("default") && RankManager.invulnerable){
						e.setCanceled(true);
						return;
					}
				}
			}
		}
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onEntityKnockbackEvent(LivingKnockBackEvent e) {
		if (Main.enabled) {
			if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
				if (!(e.getEntity() instanceof FakePlayer)) {
					if (e.getEntity() instanceof EntityPlayer) {
						String rank = RankManager.rankName((EntityPlayer)e.getEntity());
						if (rank != null && rank.equals("default") && RankManager.invulnerable){
							e.setCanceled(true);
							return;
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onEntityTakeDamage(LivingDamageEvent e) {
		if (Main.enabled) {
			if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
				if (e.getEntity() instanceof EntityPlayer && !(e.getEntity() instanceof FakePlayer)) {
					String rank = RankManager.rankName((EntityPlayer)e.getEntity());
					if (rank != null && rank.equals("default") && RankManager.invulnerable){
						e.setCanceled(true);
						return;
					}
				}
			}
		}
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onPlayerAttack(AttackEntityEvent e) {
		if (Main.enabled) {
			if (!(e.getEntityPlayer() instanceof FakePlayer)) {
				if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
					if (e.getTarget() instanceof EntityPlayer) {
						String rank = RankManager.rankName((EntityPlayer)e.getTarget());
						if (rank != null && rank.equals("default") && RankManager.invulnerable){
							e.setCanceled(true);
							return;
						}
						
						Area zone = AreaManager.getArea(e.getEntity());
						Flag flag = AreaManager.getFlag(zone, "pvp");
						if (flag != null) {
							if (flag.isEqual(false) && !AreaManager.hasBypass(e.getEntityPlayer())) {
								e.setCanceled(true);
								e.getEntityPlayer().sendMessage(new TextComponentString(TextFormatting.RED+"PVP is not allowed in this area"));
								return;	
							}
						}
					}
					if (!RankManager.hasPermission("attack-entity", e.getTarget().getName(),"",-1, e.getEntityPlayer())) {
						TextComponentString txt = new TextComponentString(TextFormatting.RED+"You don't have the permission to attack this entity");
						e.getEntityPlayer().sendMessage(txt);
						e.setCanceled(true);
						return;
					}
				}
			}
		}
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onChatEvent(ServerChatEvent e) {
		if (Main.enabled) {
			if (!(e.getPlayer() instanceof FakePlayer)) {
				NBTPlayerData data = PlayerManager.nbtplayerdata.get(e.getPlayer().getName());
				if (data.mute) {
					e.getPlayer().sendMessage(new TextComponentString(TextFormatting.RED+"You're chat restricted"));
					e.setCanceled(true);
				}
				Area zone = AreaManager.getArea(e.getPlayer());
				Flag flag = AreaManager.getFlag(zone, "can_chat");
				if (!flag.isNull()) {
					if (flag.isEqual(false) && !(AreaManager.isOwner(zone,e.getPlayer()) || AreaManager.isMember(zone,e.getPlayer()) || Util.isOp(e.getPlayer()))) {
						e.getPlayer().sendMessage(new TextComponentString(TextFormatting.RED+"You don't have the permission to send messages in this area"));
						e.setCanceled(true);
						return;	
					}
				}
				
				List players = Main.mcserver.getEntityWorld().playerEntities;
				String grade = RankManager.rankName(e.getPlayer());
				RankInfo info = RankManager.grades.get(grade);
				String nmessage = e.getMessage();
				if (RankManager.hasPermission("chat", "color","",-1, e.getPlayer())) {
					nmessage=nmessage.replace("&", "\247");
				}
				TextComponentString message = new TextComponentString(info.displayname.replace("&", "\247")+"<"+e.getUsername()+">\247f "+nmessage);
				e.setComponent(message);
				Main.logChat.info(info.displayname.replace("&", "\247")+"<"+e.getUsername()+"> "+e.getMessage());
			}
		}
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onChunkLoad(ChunkEvent.Load e) {
		if (ChunkManager.check) {
			if (!ChunkManager.analysed.contains(e.getChunk())) {
				int xs = e.getChunk().x*16;
				int zs = e.getChunk().z*16;	
				for (int x=xs; x<xs+16; x++) {
					for (int z=zs; z<zs+16; z++) {
						for (int y=0; y<Util.maxHeight(e.getWorld(), x, z); y++) {
							BlockPos pos = new BlockPos(x,y,z);
							IBlockState iblock = e.getWorld().getBlockState(pos);
							ResourceLocation loc = Block.REGISTRY.getNameForObject(iblock.getBlock());
							String id = loc.getResourceDomain() + ":" + loc.getResourcePath();
							if (ChunkManager.block2remove.contains(id+"/"+iblock.getBlock().getMetaFromState(iblock)) || ChunkManager.block2remove.contains(id+"/*")) {
								e.getWorld().setBlockToAir(pos);
							}
						}
					}
				}
				System.out.println("Clear chunk ["+e.getChunk().x+", "+e.getChunk().z+"]");
				ChunkManager.analysed.add(e.getChunk());
			}
		}
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onEntityJoin(EntityJoinWorldEvent event) {
		if (Main.enabled) {
			if (!(event.getEntity() instanceof EntityPlayer)) {
				Area area = AreaManager.getArea(event.getEntity());
				Flag flag = AreaManager.getFlag(area, "spawn_entity");
				if (!flag.isNull()) {
					ResourceLocation loc = EntityList.getKey(event.getEntity());
					String name = loc.getResourceDomain() + "." + loc.getResourcePath();
					if (flag.existPermission(name)) {
						if (flag.hasPermission(name, 0)) {
							event.setCanceled(true);
							event.getEntity().dismountRidingEntity();
							event.getEntity().setDead();
						} else {
							event.setCanceled(false);
						}
					} else if ((event.getEntity().isCreatureType(EnumCreatureType.CREATURE, false) || event.getEntity().isCreatureType(EnumCreatureType.WATER_CREATURE, false)) && flag.existPermission("creature.*")) {
						if (flag.hasPermission("creature.*", 0)) {
							event.setCanceled(true);
							event.getEntity().dismountRidingEntity();
							event.getEntity().setDead();
						} else {
							event.setCanceled(false);
						}
					} else if (event.getEntity().isCreatureType(EnumCreatureType.MONSTER, false) && flag.existPermission("monster.*")) {
						if (flag.hasPermission("monster.*", 0)) {
							event.getEntity().dismountRidingEntity();
							event.getEntity().setDead();
							event.setCanceled(true);
						} else {
							event.setCanceled(false);
						}
					}  else if (!event.getEntity().isCreatureType(EnumCreatureType.MONSTER, false) && !event.getEntity().isCreatureType(EnumCreatureType.WATER_CREATURE, false) && !event.getEntity().isCreatureType(EnumCreatureType.CREATURE, false) && flag.existPermission("object.*")) {
						if (flag.hasPermission("object.*", 0)) {
							event.getEntity().dismountRidingEntity();
							event.getEntity().setDead();
							event.setCanceled(true);
						} else {
							event.setCanceled(false);
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onExplosion(ExplosionEvent e) {
		if (Main.enabled) {
			Area zone = AreaManager.getArea(e.getWorld(), e.getExplosion().getPosition());
			Flag flag = AreaManager.getFlag(zone, "explosions");
			if (!flag.isNull()) {
				if (flag.isEqual(false)) {
					e.getExplosion().clearAffectedBlockPositions();
				}
			}
		}
	}

}
